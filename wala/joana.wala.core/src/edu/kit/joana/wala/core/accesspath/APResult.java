/**
 * This file is part of the Joana IFC project. It is developed at the
 * Programming Paradigms Group of the Karlsruhe Institute of Technology.
 *
 * For further details on licensing please read the information at
 * http://joana.ipd.kit.edu or contact the authors.
 */
package edu.kit.joana.wala.core.accesspath;

import edu.kit.joana.wala.core.accesspath.APIntraProcV2.MergeOp;
import gnu.trove.iterator.TIntIterator;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.TIntSet;

import java.util.LinkedList;
import java.util.Set;

/**
 * Result of the accesspath and merge info computation. Contains info for each PDG in the SDG.
 * 
 * @author Juergen Graf <juergen.graf@gmail.com>
 */
public class APResult {
	
	private final TIntObjectMap<APIntraprocContextManager> pdgId2ctx = new TIntObjectHashMap<>();
	private int numOfAliasEdges = 0;
	private final int rootPdgId;
	
	public APResult(final int rootPdgId) {
		this.rootPdgId = rootPdgId;
	}
	
	void add(final APIntraprocContextManager ctx, final int numOfCurAliasEdges) {
		pdgId2ctx.put(ctx.getPdgId(), ctx);
		numOfAliasEdges += numOfCurAliasEdges;
	}
	
	public APContextManagerView get(final int pdgId) {
		return pdgId2ctx.get(pdgId);
	}
	
	public APContextManagerView getRoot() {
		return get(rootPdgId);
	}

	public int getNumOfAliasEdges() {
		return numOfAliasEdges;
	}
	
	public boolean propagateInitialContextToCalls(final int startId) {
		boolean changed = false;
		final APIntraprocContextManager root = pdgId2ctx.get(startId);
		final Set<MergeOp> initial = root.computeMaxInitialContext();
		root.setInitialAlias(initial);
		final LinkedList<APIntraprocContextManager> work = new LinkedList<>();
		work.add(root);
		
		while (!work.isEmpty()) {
			final APIntraprocContextManager cur = work.removeLast();
			final TIntSet called = cur.getCalledMethods();
			final TIntIterator it = called.iterator();
			
			while (it.hasNext()) {
				final int curM = it.next();
				final APIntraprocContextManager callee = pdgId2ctx.get(curM);
				final APContext ctxCallee = cur.computeContextForAllCallsTo(callee);
				
				if (ctxCallee != null) {
					callee.setInitialAlias(ctxCallee);
					changed = true;
					if (!work.contains(callee)) {
						work.add(callee);
					}
				}
			}
		}

		return changed;
	}

	public void reset() {
		for (final APIntraprocContextManager ctx : pdgId2ctx.valueCollection()) {
			ctx.resetInitialAlias();
		}
	}
}
