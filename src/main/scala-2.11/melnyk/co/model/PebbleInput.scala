package melnyk.co.model

import com.twitter.finatra.request.FormParam
import com.twitter.inject.Logging
import org.joda.time.{DateTime, DateTimeZone}

case class PebbleInput(@FormParam data:String)

case class Trace(log:String)
case class DataRow(
                  time: DateTime, //absolute time of the line, in UTC time zone
                  steps: Int = 0, //number of steps taken in the given minute (120 in this example)
                  yaw: Int = 0, //yaw (angle of the watch in the x-y plane) in 1/16th of turn
                  pitch: Int = 0, //pitch (angle of the watchface to the z axis) in 1/16th of turn
                  vmc: Int = 0, //VMC which is a measure of how much movement happened during the minute (9915 here is a lot, my desk activity is a few thousands, light sleep or watching TV is a few hundreds, and it goes down to zero during deep sleep)
                  light: Int = 0, //ambient light level, from 1 (darkest) to 4 (brightest) with 0 meaning unknown
                  activity: Int = 0 //activity mask, currently 3 for deep sleep, 1 for non-deep sleep or 0 for not sleeping.
                  )
object DataRow extends Logging{
  private val pattern = """(^\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}(?:.\d{1,3})?Z),(\d*),(\d*),(\d*),(\d*),(\d*),(\d*)""".r


  def fromInput(in: String): Option[DataRow] = in match {
    case pattern(timestr, steps, yaw, pitch, vmx, light, activity) =>
      // implicit conversion
      implicit def strToInt(i: String):Int = if (i.isEmpty) 0 else java.lang.Integer.parseInt(i)
      implicit def strToJodaTime(i: String): DateTime = new DateTime(i, DateTimeZone.UTC)

      Some(new DataRow(timestr, steps, yaw, pitch, vmx, light, activity))
    case _ => None
  }
}
