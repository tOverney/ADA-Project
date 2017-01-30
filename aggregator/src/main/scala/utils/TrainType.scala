package utils

/**
 * The capacity values are based on what we were able to gather through
 * http://www.reisezuege.ch/reisezuege/index.php?action=1 manually
 * and used wikipedia to complete (https://en.wikipedia.org/wiki/Stadler_KISS)
 *
 * We took the mean value for all composition
 */
package object TrainTypes {

  sealed trait TrainType {
    // returns a function depending on which class (1 or 2) you want the result
    val percentageFilled: Map[String, Int => Double] = Map(
      ("1", (i: Int) =>  .5D),
      ("2", (i: Int) =>  .9D),
      ("3", (i: Int) => 1.3D)
    ).withDefault(_ => (i: Int) => .2D)

    val capacity1stClass: Int
    val capacity2ndClass: Int

    def computePassengerAmnt(first: String, second: String): Int = {
      val occ1stClass = percentageFilled(first)(1)
      val occ2ndClass = percentageFilled(second)(2)

      (capacity1stClass * occ1stClass + capacity2ndClass * occ2ndClass).toInt
    }
  }

  trait SmallRegionalTrain extends TrainType {
    val capacity2ndClass: Int = 241
    val capacity1stClass: Int = 24
  }

  case object S extends SmallRegionalTrain {
    override val percentageFilled: Map[String, Int => Double] = Map(
      ("1", (i: Int) => .5D),
      ("2", (i: Int) => .9D),
      ("3", (i: Int) => 2D)
    ).withDefault(_ => (i: Int) => .2D)
  }

  case object OtherSmallRegioTrain extends SmallRegionalTrain

  case object RE extends TrainType {
    val capacity2ndClass: Int = 1058
    val capacity1stClass: Int = 273
  }

  case object ICE extends TrainType {
    // ICE to switzerland seems to all have the same composition.
    val capacity2ndClass = 579
    val capacity1stClass = 252
  }

  case object EC extends TrainType {
    val capacity2ndClass = 364
    val capacity1stClass = 120
  }

  case object RJ extends TrainType {
    // there's only one RJ composition
    val capacity2ndClass = 316
    val capacity1stClass = 92
  }

  case object IC extends TrainType {
    val capacity2ndClass = 446
    val capacity1stClass = 174
  }

  case object ICN extends TrainType {
    val capacity2ndClass = 489
    val capacity1stClass = 160
  }

  case object EN extends TrainType {
    val capacity2ndClass = 481
    val capacity1stClass = 45
  }

  case object IR extends TrainType {
    val capacity2ndClass = 640
    val capacity1stClass = 180
  }

  case object R extends TrainType {
    val capacity2ndClass = 406
    val capacity1stClass = 120
  }

  case object TGV extends TrainType {
    val capacity2ndClass = 257
    val capacity1stClass = 121
  }

  // These are train where you can put your car on.
  case object ATZ extends TrainType {
    val capacity2ndClass = 100
    val capacity1stClass = 60
  }

  object TrainType {
    def fromLongName(trainName: String): TrainType = {
      val strType = trainName.split(' ')(0)

      strType match {
        case "S" | "SN"          => S
        case "RE" | "TE2"        => RE
        case "ICE"               => ICE
        case "EC" | "Transalpin" => EC
        case "RJ"                => RJ
        case "IC"                => IC
        case "ICN"               => ICN
        case "EN"                => EN
        case "IR"                => IR
        case "R"                 => R
        case "TGV"               => TGV
        case "ATZ"               => ATZ
        case "TER" | "RB"        => OtherSmallRegioTrain
      }
    }
  }
}
