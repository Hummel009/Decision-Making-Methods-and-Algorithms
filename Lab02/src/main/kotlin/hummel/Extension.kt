package hummel

fun <T> Array<T>.randomElement(): T? = (if (isEmpty()) null else this[RANDOM.nextInt(size)])