object MyBot {
    // There is already a basic strategy in place here. You can use it as a
    // starting point, or you can throw it out entirely and replace it with
    // your own. Check out the tutorials and articles on the contest website at
    // http://www.ai-contest.com/resources.
    def doTurn(pw:PlanetWars) {
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
            var c = System.in.read()
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
                
                c = System.in.read()
            }
        }
        catch {
            // Owned.
            case _ => Unit
        }
    }
}
