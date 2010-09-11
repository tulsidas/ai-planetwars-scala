object DualBot {
    def doTurn(pw:PlanetWars) {
        var numFleets = 1
        var attackMode = false
        if (pw.numShips(1) > pw.numShips(2)) {
            if (pw.production(1) > pw.production(2)) {
                numFleets = 1
                attackMode = true
            }
            else {
                numFleets = 3
            }
        }
        else {
            if (pw.production(1) > pw.production(2)) {
                numFleets = 1
            }
            else {
                numFleets = 5
            }
        }

        // (1) If we current have more tha numFleets fleets in flight, just do
        // nothing until at least one of the fleets arrives.
        if (pw.myFleets.size >= numFleets) {
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
