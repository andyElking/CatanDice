object test {
  def main(args: Array[String]): Unit = {
    val t = new Tabelca
    /*t.useBalancedDice = false
    for (i <- 0 until 10000) {
      print(s"#$i: ${t.toss};  ")
    }
    println()
    println()
    println(t.toString)*/
    t.useBalancedDice = true
    for (i <- 0 until 10) {
      print(s"#$i: ${t.toss};  ")
    }
    println()
    println()
    println(t.toString)
    println()
    println(t.layersToString)
    println(t.last5)
  }
}
