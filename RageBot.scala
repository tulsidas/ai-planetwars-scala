object RageBot {
   def doTurn(pw:PlanetWars) {
      pw.myPlanets.foreach { source => 
         if (source.numShips >= 10 * source.growthRate) {
            var dest:Planet = null
            var bestDistance = 999999

            pw.enemyPlanets.foreach { p => 
               val dist = pw.distance(source, p)
               if (dist < bestDistance) {
                  bestDistance = dist
                  dest = p
               }
            }

            if (dest != null) {
               pw.IssueOrder(source, dest, source.numShips)
            }
         }
      }
   }

    def main(args: Array[String]) {
        var line = ""
        var message = ""

        try {
            var c:Char = System.in.read().toChar
            while (c >= 0) {
               if (c == '\n') {
                    if (line.equals("go")) {
                        val pw = new PlanetWars(message)
                        doTurn(pw)
                        pw.FinishTurn()
                        message = ""
                    }
                    else {
                        message += line + "\n"
                    }
                    line = ""
                 }
                 else {
                    line += c
                }
                
                c = System.in.read().toChar
            }
        }
        catch {
            // Owned.
            case e => println(e)
        }
    }
}
