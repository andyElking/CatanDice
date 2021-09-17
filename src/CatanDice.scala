import java.awt
import java.awt.{Color, Dimension, Graphics}

import javax.swing.BorderFactory
import javax.swing.border.Border

import scala.swing._
import scala.swing.event._

object CatanDice extends SimpleSwingApplication {
  val tab = new Tabelca

  def top = new MainFrame {


    title = "Balanced Catan Dice"

    val popup = new Dialog {
      title = "Cannot undo"
      location = new Point(300,100)
      minimumSize = new Dimension(200,100)
      contents = new FlowPanel() {
        border = Swing.EmptyBorder(40)
        contents += new Label("No more dice throws can be undone")
      }
    }

    val tossButton = new Button("Throw the dice!")
    val balanceCheck = new CheckBox("Use balanced dice") { selected = true }
    val undoButton = new Button("Undo last throw")

    private val controls = new GridBagPanel {
      val elements = Array[Component](tossButton,balanceCheck,undoButton)

      var i = 0
      for (c <- elements) {
        layout(c) = new Constraints {
          gridx = 0; gridy = i
          anchor = GridBagPanel.Anchor.West
          insets = new Insets(5, 5, 5, 5)
        }
        i += 1
      }
    }


    val prevOutcomes = new TextPane {
      editable = false
      update()
      def update() = {
        text = tab.outcomesToStr + "\n \n" + tab.last5
      }
    }

    val table = new Table(7, 7) {
      border = BorderFactory.createLineBorder(Color.black,2)
      rowHeight = 25
      autoResizeMode = Table.AutoResizeMode.AllColumns
      fullUpdate()

      def updateVal(a: Int, b: Int) = {
        update(a + 1, b + 1, "  " + tab(a, b).toString + " ")
      }

      def fullUpdate(): Unit = {
        update(0, 0, "")
        for (i <- 1 to 6) {
          update(0, i, "  " + i.toString + "  ")
          update(i, 0, "  " + i.toString + "  ")
        }
        for (i <- 1 to 6; j <- 1 to 6) {
          update(i, j, "  " + tab(i - 1, j - 1).toString + "  ")
        }
      }
    }

    val statsPanel = new BoxPanel(Orientation.Vertical) {
      border = Swing.EmptyBorder(10)
      contents += table
      contents += prevOutcomes
    }

    contents = new FlowPanel {
      border = Swing.EmptyBorder(10)
      contents+=statsPanel
      contents+=controls
    }

    listenTo(tossButton,balanceCheck,undoButton)

    def updateStats(a: Int,b: Int): Unit = {
      table.updateVal(a,b)
      prevOutcomes.update()
    }

    reactions += {
      case ButtonClicked(`tossButton`) =>
        val x = tab.toss
        updateStats(x._1,x._2)
      case ButtonClicked(`undoButton`) =>
        val x = tab.undoToss()
        if (x == (-1,-1)) {
          popup.open
        } else {
          updateStats(x._1,x._2)
        }
      case ButtonClicked(`balanceCheck`) =>
        tab.useBalancedDice = balanceCheck.selected
    }

  }

}
