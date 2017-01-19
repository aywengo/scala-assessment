package melnyk.co.utils

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import com.twitter.util.{Future => TwitterFuture}
import com.twitter.bijection.Conversion._
import com.twitter.bijection.twitter_util.UtilBijections.twitter2ScalaFuture
import com.twitter.inject.Logging
import org.joda.time.DateTime

import scala.concurrent.{ExecutionContextExecutor, Future => ScalaFuture}
import scala.concurrent.duration._

// in order to reduce boilerplate code
trait AkkaExtensions extends Logging{
  implicit val timeout = Timeout(10.seconds)

  // generic converter scala.concurrent.Future to com.twitter.util.Future
  def ->[T](in: ScalaFuture[T])(implicit executionContext:ExecutionContextExecutor): TwitterFuture[T] = in.as[TwitterFuture[T]]

  // ask pattern wrapped in TwitterFuture
  def -?>(actor:ActorRef, message: Any)(implicit timeout: Timeout, sender: ActorRef = Actor.noSender, executionContext:ExecutionContextExecutor): TwitterFuture[Any] =
    actor.?(message).as[TwitterFuture[Any]]

  // tell wrapped in TwitterFuture
  def -!>(actor:ActorRef, message: Any)(implicit timeout: Timeout, sender: ActorRef = Actor.noSender, executionContext:ExecutionContextExecutor): TwitterFuture[Any] =
    TwitterFuture (actor.!(message))

  // tell wrapped in TwitterFuture
  // Actor per request approach
  def --!>(props:Props, message: Any)(implicit timeout: Timeout, actorSystem: ActorSystem, executionContext:ExecutionContextExecutor): TwitterFuture[Any] =
    {
      val name = DateTime.now().toString // Any other name
      debug(s"Actor named $name was created.")
      val actor = actorSystem.actorOf(props, name)
      TwitterFuture (actor.!(message))
    }

  // ask pattern wrapped in TwitterFuture
  // Actor per request approach
  def --?>(props:Props, message: Any)(implicit timeout: Timeout, actorSystem: ActorSystem, executionContext:ExecutionContextExecutor): TwitterFuture[Any] =
  {
    val name = DateTime.now().toString // Any other name
    debug(s"Actor named $name was created.")
    val actor = actorSystem.actorOf(props, name)
    actor.?(message).as[TwitterFuture[Any]]
  }
}
