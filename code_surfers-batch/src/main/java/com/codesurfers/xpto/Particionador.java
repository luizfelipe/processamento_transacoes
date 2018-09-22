
package com.codesurfers.xpto;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

/**
 * Particionador do reader.
 * 
 * @author egon.ssena
 *
 */
@Component("particionador")
public class Particionador implements Partitioner {

	public static final String PARTICAO_INDICE = "particao.indice";
	public static final String PARTICAO_TAMANHO = "particao.tamanho";

	/**
	 * Realiza o particionamento dos dados de leitura.
	 */
	@Override
	public Map<String, ExecutionContext> partition(int gridSize) {
		final Map<String, ExecutionContext> contextMap = new HashMap<>();
		for (int i = 0; i < gridSize; i++) {
			ExecutionContext context = new ExecutionContext();
			context.putInt(PARTICAO_INDICE, i);
			context.putInt(PARTICAO_TAMANHO, gridSize);
			contextMap.put(getPartitionName(i), context);
		}
		return contextMap;
	}

	private static String getPartitionName(int index) {
		return String.format("partition-%d", index);
	}

}