package com.here.fox;

import java.lang.reflect.Field;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;

import com.here.fox.model.Graph;
import com.here.fox.model.VectorNode;

public abstract class AbstractTest {
	@SuppressWarnings("unchecked")
	@AfterEach
	/**
	 * Graph is a Singelton and cannot be re-initialized nor modified. There is no
	 * straight way to reset the graph at runtime. Had to use reflection to overcome
	 * that limitation.
	 * 
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	void resetData() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		try {
			Field vectorMapField = Graph.class.getDeclaredField("vectorMap");
			vectorMapField.setAccessible(true);
			((Map<String, VectorNode>) vectorMapField.get(Graph.getInstance())).clear();
		} catch (IllegalStateException e) {
			// Silently ignore, the Graph may have been faulty and not initislised.
		}

		Field instanceField = Graph.class.getDeclaredField("INSTANCE");
		instanceField.setAccessible(true);
		instanceField.set(null, null);

	}
}
