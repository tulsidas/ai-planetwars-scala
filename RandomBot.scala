import scala.util.Random

object RandomBot {
    def doTurn(pw:PlanetWars) {
        // (1) If we current have a fleet in flight, then do nothing until it
        // arrives.
        if (pw.myFleets.size >= 1) {
            return
        }

        // (2) Pick one of my planets at random.
        val r = new Random()
        
        var source:Planet = null
        val p = pw.myPlanets
        if (p.size > 0) {
            source = p(r.nextInt(p.size))
        }

        // (3) Pick a target planet at random.
        var dest:Planet = null
        if (pw.planets.size > 0) {
            source = pw.planets(r.nextInt(pw.planets.size))
        }

        // (4) Send half the ships from source to dest.
        if (source != null && dest != null) {
            val numShips = source.numShips / 2
            pw.IssueOrder(source, dest, numShips)
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
