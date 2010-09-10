case class Fleet(val owner: Int, var numShips: Int, val sourcePlanet: Int,
   val destinationPlanet: Int, val totalTripLength: Int, var turnsRemaining: Int) 
      extends Ordered[Fleet] {

   override def compare(that:Fleet):Int = this.numShips - that.numShips
   
   def removeShips(amount:Int) = numShips -= amount
   
   def timeStep() = turnsRemaining = if (turnsRemaining > 0) turnsRemaining - 1 else 0
}
