package hummel

import org.encog.engine.network.activation.ActivationSigmoid
import org.encog.ml.data.basic.BasicMLDataSet
import org.encog.neural.networks.BasicNetwork
import org.encog.neural.networks.layers.BasicLayer
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation

fun createNetwork(inputCount: Int, outputsCount: Int): BasicNetwork {
	val network = BasicNetwork()
	network.addLayer(BasicLayer(null, true, inputCount))
	network.addLayer(BasicLayer(inputCount))
	network.addLayer(BasicLayer(ActivationSigmoid(), false, outputsCount))
	network.structure.finalizeStructure()
	return network
}

fun BasicNetwork.train(inputs: List<IntArray>, output: Int) {
	val dataSet = BasicMLDataSet(Array(inputs.size) { i ->
		DoubleArray(inputs[i].size) { j -> inputs[i][j].toDouble() }
	}, Array(inputs.size) { doubleArrayOf(output.toDouble()) })
	val train = ResilientPropagation(this, dataSet)

	var epoch = 1
	do {
		train.iteration()
		println("Epoch #" + epoch + " Error:" + train.error)
		epoch++
	} while (train.error > 0.01)
	train.finishTraining()
}