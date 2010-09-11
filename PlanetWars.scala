class PlanetWars(gameStateString: String) {
   val planets = scala.collection.mutable.ListBuffer[Planet]()
   val fleets = scala.collection.mutable.ListBuffer[Fleet]()
   
   ParseGameState(gameStateString)

   def numPlanets = planets.size
   def getPlanet(id:Int) = planets(id)

   def numFleets = fleets.size
   def getFleet(id:Int) = fleets(id)
   
   def myPlanets = playerPlanets(1)
   def neutralPlanets = playerPlanets(0)
   def enemyPlanets = planets.filter(_.owner >= 2)
   def notMyPlanets = planets.filter(_.owner != 1)
   
   def myFleets = fleets.filter(_.owner == 1)
   def enemyFleets = fleets.filter(_.owner != 1)
   
   def distance(source:Int, destination:Int):Int = distance(getPlanet(source), getPlanet(destination))

   def distance(source:Planet, destination:Planet):Int = {
      val dx: Double = source.x - destination.x
      val dy: Double = source.y - destination.y
      scala.math.ceil(scala.math.sqrt(dx * dx + dy * dy)).toInt
   }
   
    // Sends an order to the game engine. An order is composed of a source
    // planet number, a destination planet number, and a number of ships. A
    // few things to keep in mind:
    // * you can issue many orders per turn if you like.
    // * the planets are numbered starting at zero, not one.
    // * you must own the source planet. If you break this rule, the game
    // engine kicks your bot out of the game instantly.
    // * you can't move more ships than are currently on the source planet.
    // * the ships will take a few turns to reach their destination. Travel
    // is not instant. See the Distance() function for more info.
    def IssueOrder(sourcePlanet:Int, destinationPlanet:Int, numShips:Int) {
        System.out.println("" + sourcePlanet + " " + destinationPlanet + " " + numShips)
        System.out.flush()
    }

    // Sends an order to the game engine. An order is composed of a source
    // planet number, a destination planet number, and a number of ships. A
    // few things to keep in mind:
    // * you can issue many orders per turn if you like.
    // * the planets are numbered starting at zero, not one.
    // * you must own the source planet. If you break this rule, the game
    // engine kicks your bot out of the game instantly.
    // * you can't move more ships than are currently on the source planet.
    // * the ships will take a few turns to reach their destination. Travel
    // is not instant. See the Distance() function for more info.
    def IssueOrder(source:Planet, dest:Planet, numShips:Int) {
        System.out.println("" + source.id + " " + dest.id + " " + numShips)
        System.out.flush()
    }

    // Sends the game engine a message to let it know that we're done sending
    // orders. This signifies the end of our turn.
    def FinishTurn() {
        System.out.println("go")
        System.out.flush()
    }

   def isAlive(playerID:Int) = planets.count(_.owner == playerID) > 0 || fleets.count(_.owner == playerID) > 0

   def winner:Int = {
      val players = scala.collection.mutable.HashSet[Int]()
      
      players ++= planets.map(_.owner)
      players ++= fleets.map(_.owner)

      players.size match {
         case 0 => 0
         case 1 => players.head
         case _ => -1
      }
   }
   
   def numShips(pid:Int) = playerPlanets(pid).foldLeft(0)(_ + _.numShips) + playerFleets(pid).foldLeft(0)(_ + _.numShips)

   def production(pid:Int) = playerPlanets(pid).foldLeft(0)(_ + _.growthRate)
   
   private def playerPlanets(pid:Int) = planets.filter(_.owner == pid)
   private def playerFleets(pid:Int) = fleets.filter(_.owner == pid)

    // Parses a game state from a string. On success, returns 1. On failure, // returns 0.
   def ParseGameState(s: String):Int = {
      planets.clear()
      fleets.clear()

      var planetID = 0

      val lines = s.split("\n")
      for (i <- 0 until lines.length) {
         var line = lines(i)
         val commentBegin = line.indexOf('#')

         if (commentBegin >= 0) {
             line = line.substring(0, commentBegin)
         }

         if (line.trim().length() > 0) {
            val tokens = line.split(" ")

            if (tokens.length > 0) {
               if (tokens(0).equals("P")) {
                   if (tokens.length != 6) {
                       return 0
                   }

                   val x = java.lang.Double.parseDouble(tokens(1))
                   val y = java.lang.Double.parseDouble(tokens(2))
                   val owner = java.lang.Integer.parseInt(tokens(3))
                   val numShips = java.lang.Integer.parseInt(tokens(4))
                   val growthRate = java.lang.Integer.parseInt(tokens(5))
                   
                   planets += new Planet(planetID, owner, numShips, growthRate, x, y)

                   planetID = planetID + 1 
               }
               else if (tokens(0).equals("F")) {
                   if (tokens.length != 7) {
                       return 0
                   }

                   val owner = java.lang.Integer.parseInt(tokens(1))
                   val numShips = java.lang.Integer.parseInt(tokens(2))
                   val source = java.lang.Integer.parseInt(tokens(3))
                   val destination = java.lang.Integer.parseInt(tokens(4))
                   val totalTripLength = java.lang.Integer.parseInt(tokens(5))
                   val turnsRemaining = java.lang.Integer.parseInt(tokens(6))
                   
                   fleets += new Fleet(owner, numShips, source, destination, totalTripLength, turnsRemaining)
               }
               else {
                   return 0
               }
            }
         }
     }

     return 1
   }
}
