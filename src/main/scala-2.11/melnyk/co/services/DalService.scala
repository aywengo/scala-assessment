package melnyk.co.services

import javax.inject.Inject

import melnyk.co.model.DataRow


class DalService @Inject() () {
  // example of blocking io - like a query to db
  def fetchData() =
    List(
      DataRow.fromInput("2016-05-25T21:22:00Z,120,0,4,9915,1,0"),
      DataRow.fromInput("2016-05-25T21:23:00Z,10,0,4,9915,1,0"),
      DataRow.fromInput("2016-05-25T21:24:00Z,20,0,4,9915,1,0"),
      DataRow.fromInput("2016-05-25T21:25:00Z,12,0,4,9915,1,0"),
      DataRow.fromInput("2016-05-25T21:26:00Z,,,1,,1,0")
    )
}


