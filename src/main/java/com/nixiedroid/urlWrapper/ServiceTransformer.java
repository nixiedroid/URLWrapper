package com.nixiedroid.urlWrapper;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

public class ServiceTransformer implements ClassFileTransformer {
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        if ("java/net/URL".equals(className)) {
            try {
                return transformUrl(classfileBuffer);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
        return null;
    }

    private byte[] transformUrl(byte[] basicClass) {
        ClassReader reader = new ClassReader(basicClass);
        ClassNode classNode = new ClassNode();
        reader.accept(classNode, ClassReader.SKIP_DEBUG);
        MethodNode method = classNode.methods
                .stream()
                .filter(m -> m.name.equals("openConnection") && m.desc.equals("(Ljava/net/Proxy;)Ljava/net/URLConnection;"))
                .findAny()
                .orElseThrow(() -> new NoSuchMethodError("Method openConnection(Proxy) not found in URL class"));
        attachURLHook(method);
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        classNode.accept(writer);
        return writer.toByteArray();
    }

    private void attachURLHook(MethodNode method) {
        InsnList list = new InsnList();
        list.add(new VarInsnNode(Opcodes.ALOAD, 0));
        list.add(new VarInsnNode(Opcodes.ALOAD, 1));
        list.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
                "com/nixiedroid/urlWrapper/URLHook",
                "openConnection",
                "(Ljava/net/URL;Ljava/net/Proxy;)Ljava/net/URLConnection;",
                false));
        list.add(new InsnNode(Opcodes.DUP));
        LabelNode lab = new LabelNode();
        list.add(new JumpInsnNode(Opcodes.IFNULL, lab));
        list.add(new InsnNode(Opcodes.ARETURN));
        list.add(lab);
        list.add(new InsnNode(Opcodes.POP));
        method.instructions.insert(list);
    }
}
