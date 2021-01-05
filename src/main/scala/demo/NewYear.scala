package demo

case class NewYear(year: Int, wish: String) {
  def gift: String = "A fairy pony"
}

val year = NewYear(2020, "I wish Scala 3 was released soon")

val TableName = "new-years"
