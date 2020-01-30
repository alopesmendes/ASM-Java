package fr.umlv.retro.features;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.objectweb.asm.ClassVisitor;

import fr.umlv.retro.options.OptionParsing;

public interface Feature {
	
	/**
	 * @param origin
	 * @param feature
	 * @return true if origin equals feature.
	 */
	static boolean detect(String origin, String feature) {
		return origin.equals(feature);
	}
	
<<<<<<< HEAD
	/**
	 * @param api
	 * @param classVisitor
	 * @param options
	 * @return
	 */
	static ClassVisitor createWriterFeauture(int api, ClassVisitor classVisitor, OptionParsing options) {
		return FeatureImpl.createClassVisitor(api, classVisitor, options);
	}
	
	static class FeatureImpl {
		private final Map<Class<? extends Feature>, Function<ClassVisitor, ClassVisitor>> featureVisitors;
		
		
		
		/**
		 * @param featureVisitors
		 */
		private FeatureImpl(Map<Class<? extends Feature>, Function<ClassVisitor, ClassVisitor>> featureVisitors) {
			this.featureVisitors = featureVisitors;
		}



		/**
		 * @param api
		 * @return
		 */
		private static FeatureImpl defaultFeatureImpl(int api) {
			HashMap<Class<? extends Feature>, Function<ClassVisitor, ClassVisitor>> map = new HashMap<>();
			map.put(Concat.class, classVisitor -> new Concat(api, classVisitor));
			map.put(Nestmates.class, classVisitor -> new Nestmates(api, classVisitor));
			map.put(Record.class, classVisitor -> new Record(api, classVisitor));
			map.put(Lambdas.class, classVisitor -> new Lambdas(api, classVisitor));
			map.put(TryWithRessources.class, classVisitor -> new TryWithRessources(api, classVisitor));
			return new FeatureImpl(map);
		}
		
		private static ClassVisitor defaultVisitor(int api, ClassVisitor classVisitor, FeatureImpl featureImpl) {
			for (var value : featureImpl.featureVisitors.values()) {
				classVisitor = value.apply(classVisitor); 
			}
			return classVisitor;
		}
		
		/**
		 * @param api
		 * @param classVisitor
		 * @param options
		 * @return
		 */
		static ClassVisitor createClassVisitor(int api, ClassVisitor classVisitor, OptionParsing options) {
			FeatureImpl featureImpl = defaultFeatureImpl(api);
			List<Class<? extends Feature>> features = options.getFeatures();
			if (features.isEmpty()) { return defaultVisitor(api, classVisitor, featureImpl); }
			for (var e : featureImpl.featureVisitors.entrySet()) {
				if (features.contains(e.getKey())) { classVisitor = e.getValue().apply(classVisitor); }
			}
			return classVisitor;
		}
=======
	static ClassVisitor createWriterFeauture(int api, ClassVisitor classVisitor) {
		
		return new Concat(api, classVisitor);
>>>>>>> 1645978403836fa67a97cf04ce1affe0e8865397
	}
	
	
	
}
