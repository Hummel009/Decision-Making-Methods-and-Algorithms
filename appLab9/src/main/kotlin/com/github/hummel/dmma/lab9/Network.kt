package com.github.hummel.dmma.lab9

import org.encog.engine.network.activation.ActivationSigmoid
import org.encog.ml.data.basic.BasicMLDataSet
import org.encog.neural.networks.BasicNetwork
import org.encog.neural.networks.layers.BasicLayer
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation

fun createNetwork(inputCount: Int, outputsCount: Int): BasicNetwork {
	val network = BasicNetwork()

	// Добавляем входной слой с указанным количеством нейронов
	network.addLayer(BasicLayer(null, true, inputCount))

	// Добавляем скрытый слой с указанным количеством нейронов
	network.addLayer(BasicLayer(inputCount))

	// Добавляем выходной слой с сигмоидной функцией активации и указанным количеством нейронов
	// Сигмоидная функция используется в нейронных сетях для введения нелинейности в выходной сигнал.
	network.addLayer(BasicLayer(ActivationSigmoid(), false, outputsCount))

	network.structure.finalizeStructure()
	return network
}

fun BasicNetwork.train(inputs: List<IntArray>, output: Int) {
	val dataSet = BasicMLDataSet(Array(inputs.size) { i ->
		DoubleArray(inputs[i].size) { j ->
			inputs[i][j].toDouble()
		}
	}, Array(inputs.size) {
		doubleArrayOf(output.toDouble())
	})

	val train = ResilientPropagation(this, dataSet)

	var step = 1
	do {
		train.iteration()
		println("Step #$step; Error: ${train.error}")
		step++
	} while (train.error > 0.01)
	train.finishTraining()
}