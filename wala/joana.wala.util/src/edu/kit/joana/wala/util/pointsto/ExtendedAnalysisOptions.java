/**
 * This file is part of the Joana IFC project. It is developed at the
 * Programming Paradigms Group of the Karlsruhe Institute of Technology.
 *
 * For further details on licensing please read the information at
 * http://joana.ipd.kit.edu or contact the authors.
 */
package edu.kit.joana.wala.util.pointsto;

import com.ibm.wala.ipa.callgraph.*;

import edu.kit.joana.wala.util.pointsto.ObjSensZeroXCFABuilder.MethodFilter;

/**
 * AnalysisOptions with additional attributes for Joana specific options.
 * 
 * @author Juergen Graf <graf@kit.edu>
 */
public class ExtendedAnalysisOptions extends AnalysisOptions {

	public MethodFilter filter;

	public ExtendedAnalysisOptions(final MethodFilter filter, final AnalysisScope scope, final Iterable<? extends Entrypoint> e, UninitializedFieldHelperOptions fieldHelperOptions) {
		super(scope, e, fieldHelperOptions, InterfaceImplementationOptions.createEmpty());
		if (filter == null) {
			this.filter = new ObjSensZeroXCFABuilder.DefaultMethodFilter();
		} else {
			this.filter = filter;
		}
	}

	public ExtendedAnalysisOptions(final MethodFilter filter, final AnalysisScope scope, final Iterable<? extends Entrypoint> e) {
		this(filter, scope, e, UninitializedFieldHelperOptions.createEmpty());
	}

	public ExtendedAnalysisOptions(final AnalysisScope scope, final Iterable<? extends Entrypoint> e) {
		this(null, scope, e);
	}
	
	public ExtendedAnalysisOptions(final MethodFilter filter) {
		this(filter, null, null);
	}

}
