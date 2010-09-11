object BullyBot {
    def doTurn(pw:PlanetWars) {
        // (1) If we current have a fleet in flight, just do nothing.
        if (pw.myFleets.size >= 1) {
            return
        }

        // (2) Find my strongest planet.
        val source = pw.myPlanets.reduceLeft((p1, p2) => if (p1.numShips > p2.numShips) p1 else p2)

        // (3) Find the weakest enemy or neutral planet.
        val dest = pw.notMyPlanets.reduceLeft {
         (p1, p2) => 
            val s1 = 1 / (1 + p1.numShips)
            val s2 = 1 / (1 + p2.numShips)

            if (s1 > s2) p1 else p2
        }

        // (4) Send half the ships from my strongest planet to the weakest
        // planet that I do not own.
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
