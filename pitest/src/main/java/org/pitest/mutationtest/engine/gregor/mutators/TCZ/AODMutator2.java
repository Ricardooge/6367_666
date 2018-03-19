package org.pitest.mutationtest.engine.gregor.mutators.myMutator;

import java.util.HashMap;
import java.util.Map;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.pitest.mutationtest.engine.gregor.AbstractInsnMutator;
import org.pitest.mutationtest.engine.gregor.InsnSubstitution;
import org.pitest.mutationtest.engine.gregor.MethodInfo;
import org.pitest.mutationtest.engine.gregor.MethodMutatorFactory;
import org.pitest.mutationtest.engine.gregor.MutationContext;
import org.pitest.mutationtest.engine.gregor.ZeroOperandMutation;

public enum AODMutator2 implements MethodMutatorFactory {
	AOD_MUTATOR2;
	
	public MethodVisitor create(final MutationContext context, final MethodInfo methodInfo, final MethodVisitor methodVisitor) {
	    return new   AODMethodVisitor(this, methodInfo,context, methodVisitor);
	  }
	
	public String getGloballyUniqueId() {
	    return this.getClass().getName();
	  }
	
	public String getGloballyUniqueId() {
	    return this.getClass().getName();
	  }
}


class AODMathodVisitor extends AbstractInsnMutator {
	
	AODMethodVisitor(final MethodMutatorFactory factory, final MethodInfo methodInfo, final MutationContext context,final MethodVisitor writer) {
	    super(factory, methodInfo, context, writer);
	}

	  
	
	protected Map<Integer, ZeroOperandMutation> getMutations() {
		    return MUTATIONS;
		  }
	
	 private static final Map<Integer, ZeroOperandMutation> MUTATIONS = new HashMap<Integer, ZeroOperandMutation>();
	 
	 static {
		 MUTATIONS.put(Opcodes.IADD, new InsnSubstitution(Opcodes.NOP, "AOD IADD Replaced: Remove + "));
		 MUTATIONS.put(Opcodes.ISUB, new InsnSubstitution(Opcodes.NOP, "AOD ISUB Replaced: Remove - "));
		 MUTATIONS.put(Opcodes.IMUL, new InsnSubstitution(Opcodes.NOP, "AOD IMUL Replaced: Remove * "));
		 MUTATIONS.put(Opcodes.IDIV, new InsnSubstitution(Opcodes.NOP, "AOD IDIV Replaced: Remove / "));
		 MUTATIONS.put(Opcodes.IREM, new InsnSubstitution(Opcodes.NOP, "AOD IREM Replaced: Remove % "));
		 MUTATIONS.put(Opcodes.LADD, new InsnSubstitution(Opcodes.NOP2, "AOD LADD Replaced: Remove + "));
		 MUTATIONS.put(Opcodes.LSUB, new InsnSubstitution(Opcodes.NOP2, "AOD LSUB Replaced: Remove - "));
		 MUTATIONS.put(Opcodes.LMUL, new InsnSubstitution(Opcodes.NOP2, "AOD LMUL Replaced: Remove * "));
		 MUTATIONS.put(Opcodes.LDIV, new InsnSubstitution(Opcodes.NOP2, "AOD LDIV Replaced: Remove / "));
		 MUTATIONS.put(Opcodes.LREM, new InsnSubstitution(Opcodes.NOP2, "AOD LREM Replaced: Remove % "));
		 MUTATIONS.put(Opcodes.FADD, new InsnSubstitution(Opcodes.NOP, "AOD FADD Replaced: Remove + "));
		 MUTATIONS.put(Opcodes.FSUB, new InsnSubstitution(Opcodes.NOP, "AOD FSUB Replaced: Remove - "));
		 MUTATIONS.put(Opcodes.FMUL, new InsnSubstitution(Opcodes.NOP, "AOD FDIV Replaced: Remove * "));
		 MUTATIONS.put(Opcodes.FDIV, new InsnSubstitution(Opcodes.NOP, "AOD FREB Replaced: Remove / "));
		 MUTATIONS.put(Opcodes.FREM, new InsnSubstitution(Opcodes.NOP, "AOD FREM Replaced: Remove % "));
		 MUTATIONS.put(Opcodes.DADD, new InsnSubstitution(Opcodes.NOP2, "AOD DADD Replaced: Remove + "));
		 MUTATIONS.put(Opcodes.DSUB, new InsnSubstitution(Opcodes.NOP2, "AOD DSUB Replaced: Remove - "));
		 MUTATIONS.put(Opcodes.DMUL, new InsnSubstitution(Opcodes.NOP2, "AOD DMUL Replaced: Remove * "));
		 MUTATIONS.put(Opcodes.DDIV, new InsnSubstitution(Opcodes.NOP2, "AOD DDIV Replaced: Remove / "));
		 MUTATIONS.put(Opcodes.DREM, new InsnSubstitution(Opcodes.NOP2, "AOD DREM Replaced: Remove % "));
		 
	 }
	 
}
	 
	 
	 
	 