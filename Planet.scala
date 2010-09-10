case class Planet(val id: Int, var owner: Int, var numShips: Int, 
   var growthRate: Int, val x: Double, val y: Double) {

   def addShips(amount:Int) = numShips += amount
   def removeShips(amount:Int) = numShips -= amount
}
