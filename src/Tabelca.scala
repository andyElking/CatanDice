import scala.collection.mutable

class Tabelca {

  private val tab: Array[Array[Int]] = Array.fill[Int](6,6)(0)
  private var max: Int = 0
  private var min: Int = 0
  private var total: Int = 0
  private val rand = new scala.util.Random()

  var history: List[(Short,Short)] = List[(Short,Short)]()

  var useBalancedDice: Boolean = true

  def apply(a: Int, b: Int): Int = tab(a)(b)
  def giveTable: Array[Array[Int]] = tab.clone()

  private def seq(n: Int): Int = tab(n/6)(n%6)

  /*private def padToMax(str: String) = {
    val mlen = max.toString.length
    val len = str.length
    if (len<mlen) {
      str+ (" "*(mlen-len))
    } else {
      str
    }
  }*/

  override def toString: String = {
    val len = max.toString.length
    val horizBound: String = "-"*(len+2) + "+"
    val horiz: String = "---+" + horizBound*6
    var str: String = "     "
    for (j: Int <- 0 until 6) {
      str += (j+1).toString.padTo(len,' ') + " | "
    }
    str += "\n"+horiz + "\n"
    for (i <- 0 until 6) {
      str += s" ${i+1} | "
      for (j <- 0 until 6) {
        str += (tab(i)(j)).toString.padTo(len,' ') + " | "
      }
      str = str.stripSuffix(" ")
      str+="\n"+horiz+"\n"
    }
    str
  }

  private val layers: mutable.ArrayDeque[Int] = mutable.ArrayDeque(36)

  def layersToString = layers.mkString(", ")

  private def decrease(a: Int,b: Int): Unit = {
    assert(tab(a)(b)>=1)
    tab(a)(b)-=1
    val c = tab(a)(b)
    if (c<min) {
      min-=1
      layers.prepend(0)
    } else if (c==max-1 && layers(c-min)>=35) {
      layers.removeLast()
      max-=1
    }
    layers(c-min)+=1
  }

  private def increase(a: Int,b: Int): Unit = {
    val count = tab(a)(b)
    layers(count-min)-=1
    assert(layers(count-min)>=0)
    tab(a)(b)+=1
    total+=1
    if (count == min && layers(0) == 0) {
      layers.removeHead()
      min+=1
    } else if (count == max) {
      layers.append(36)
      max+=1
    }
  }

  private def randLayer = rand.nextInt(layers.length)

  private def randDiceInLayer(count: Int): (Int,Int) = {
    val idx = rand.nextInt(layers(count-min))
    var i = 0
    var j = -1
    while (i <= idx) {
      j+=1
      if (seq(j) <= count) i+=1
    }
    (j/6,j%6)
  }

  def toss: (Int,Int) = {
    var a: Int = 0
    var b: Int = 0
    if (useBalancedDice) {
      val layer = randLayer
      val dice = randDiceInLayer(layer + min)
      a = dice._1
      b = dice._2
      increase(a,b)
    } else {
      a = rand.nextInt(6)
      b = rand.nextInt(6)
      increase(a,b)
    }
    history = (a.toShort,b.toShort)::history
    (a,b)
  }

  def undoToss(): (Int,Int) = {
    var a = 0
    var b = 0
    history match {
      case x::xs =>
        a = x._1.toInt
        b= x._2.toInt
        history = xs
      case Nil => return (-1,-1)
    }
    decrease(a,b)
    (a,b)
  }

  def last5: String = history.take(5).map( x => x._1+x._2+2 ).mkString("Last 5 tosses (most recent first): ",", ","")

  def outcomeDist: Array[Int] = {
    val res = Array.fill[Int](11)(0)
    for(i <- 0 until 6; j <- 0 until 6) {
      res(i+j)+=tab(i)(j)
    }
    res
  }

  def outcomesToStr: String = {
    var res = ""
    val arr = outcomeDist
    for (i <- 0 until 11) {
      res+= s"${i+2}: ${arr(i)};  "
    }
    res
  }
}
