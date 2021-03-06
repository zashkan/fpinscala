import org.specs2.mutable.Specification
import org.specs2.ScalaCheck

import fpinscala.laziness._

class StreamSpec extends Specification with ScalaCheck {
 
 //========================================================================================
 //#5.1
 val streamEmpty = Stream()
 val streamOfInts = Stream(1,2,3)

  "toList" should {
    "return an empty List for an empty Stream" in {
      streamEmpty.toList mustEqual List()
    }

    "return a List with entires in the Stream with the same order" in {
      streamOfInts.toList mustEqual List(1,2,3)
    }
  }

 //========================================================================================
 //#5.2
  "take" should {
    "return an empty Stream for an empty Stream" in {
      streamEmpty.take(3) mustEqual Empty
    }

    "return a Stream with n first entires" in {
      streamOfInts.take(1).toList mustEqual Stream(1).toList
    }
  }

  "drop" should {
    "return an empty Stream for an empty Stream" in {
      streamEmpty.drop(3) mustEqual Empty
    }

    "return an empty Stream when n is bigger than or equal to Stream length" in {
      streamOfInts.drop(4) mustEqual Empty
    }

    "return a Stream with n first entires removed" in {
      streamOfInts.drop(1).toList mustEqual Stream(2,3).toList
    }
  }

 //========================================================================================
 //#5.3
  "takeWhile" should {
    "return an empty Stream for an empty Stream" in {
      streamEmpty.takeWhile(_ == 1) mustEqual Empty
    }

    "return an empty Stream when the first entry does not satisfy predicate" in {
      streamOfInts.takeWhile(_ == 2) mustEqual Empty
    }

    "return a Stream with n first entires that satisfy predicate" in {
      streamOfInts.takeWhile(_ == 1).toList mustEqual Stream(1).toList
    }
  }

 //========================================================================================
 //#5.4
  "forAll" should {
    "return true for an empty Stream" in {
      streamEmpty.forAll(_ == 1) mustEqual true
    }

    "return true when all entries satisfy predicate" in {
      streamOfInts.forAll(_ < 4) mustEqual true
    }

    "return false when some entry does not satisfy predicate" in {
      streamOfInts.forAll(_ < 3) mustEqual false
      streamOfInts.forAll(_ < 2) mustEqual false
    }
  }

 //========================================================================================
 //#5.5
  "takeWhile_via_foldRight" should {
    "return an empty Stream for an empty Stream" in {
      streamEmpty.takeWhile_via_foldRight(_ == 1) mustEqual Empty
    }

    "return an empty Stream when the first entry does not satisfy predicate" in {
      streamOfInts.takeWhile_via_foldRight(_ == 2) mustEqual Empty
    }

    "return a Stream with n first entires that satisfy predicate" in {
      streamOfInts.takeWhile_via_foldRight(_ == 1).toList mustEqual Stream(1).toList
    }
  }

 //========================================================================================
 //#5.6
  "headOption_via_foldRight" should {
    "return an None for an empty Stream" in {
      streamEmpty.headOption_via_foldRight mustEqual None
    }

    "return head entry for a none-empty Stream" in {
      streamOfInts.headOption_via_foldRight mustEqual Some(1)
    }
  }

 //========================================================================================
 //#5.7
  "map" should {
    "return an empty Stream for an empty Stream" in {
      streamEmpty.map(identity) mustEqual Empty
    }

    "return a Stream with all entries transformed by f" in {
      streamOfInts.map(_ * 2).toList mustEqual List(2,4,6)
    }
  }

  "filter" should {
    "return an empty Stream for an empty Stream" in {
      streamEmpty.filter(_ == 2) mustEqual Empty
    }

    "return a Stream with only entries that satisfy the filter" in {
      streamOfInts.filter(_%2 == 1).toList mustEqual List(1,3)
    }
  }

  "append" should {
    "return an empty Stream for two empty Streams" in {
      streamEmpty.append(streamEmpty) mustEqual Empty
    }

    "return a Stream with the other Stream is empty" in {
      streamOfInts.append(streamEmpty).toList mustEqual List(1,2,3)
      streamEmpty.append(streamOfInts).toList mustEqual List(1,2,3)
    }

    "return a Stream with both none-empty Streams" in {
      streamOfInts.append(streamOfInts).toList mustEqual List(1,2,3,1,2,3)
    }
  }

  "flatMap" should {
    "return an empty Stream an empty Stream" in {
      streamEmpty.flatMap(x => Stream(x,x)) mustEqual Empty
    }

    "return a Stream with each entry mapped to a new Stream for a none-empty Stream" in {
      streamOfInts.flatMap(x => Stream(x,x)).toList mustEqual List(1,1,2,2,3,3)
    }
  }

 //========================================================================================
 //#5.8
  "constant" should {
    "return an infinite Stream with entries equal to the value passed" in {
      prop {
        x: Int =>
          Stream.constant(x).exists(_ == x) mustEqual true
          Stream.constant(x).take(2).toList mustEqual List(x,x)
      }
    }
  }  

 //========================================================================================
 //#5.9
  "from" should {
    "return an infinite Stream with entries increasing from the initial integer" in {
      prop {
        x: Int =>
          Stream.from(x).take(3).toList mustEqual List(x,x+1,x+2)
      }
    }
  }  

 //========================================================================================
 //#5.10
  "fibs" should {
    "return an infinite Stream with entry at location n being equal to fibunacci number n" in {
      prop {
        x: Int =>
           if ((x < 100) && (x > 0))
             Stream.fibs.take(x).toList.last mustEqual fpinscala.gettingstarted.MyModule.fib(x-1)
           else 
             true mustEqual true
        }
      }
    }
    
 //========================================================================================
 //#5.11 
 //#5.12
  "ones_via_unfold" should {
    "return an infinite Stream of ones" in {
      Stream.ones_via_unfold.take(4).toList mustEqual List(1,1,1,1)
    }

    "return a Stream with all entries transformed by f" in {
      streamOfInts.map(_ * 2).toList mustEqual List(2,4,6)
    }
  }

  "constant_via_unfold" should {
    "return an infinite Stream with entries equal to the value passed" in {
      prop {
        x: Int =>
          Stream.constant_via_unfold(x).exists(_ == x) mustEqual true
          Stream.constant_via_unfold(x).take(2).toList mustEqual List(x,x)
      }
    }
  }  

  "from_via_unfold" should {
    "return an infinite Stream with entries increasing from the initial integer" in {
      prop {
        x: Int =>
          Stream.from_via_unfold(x).take(3).toList mustEqual List(x,x+1,x+2)
      }
    }
  }  

  "fibs_via_unfold" should {
    "return an infinite Stream with entry at location n being equal to fibunacci number n" in {
      prop {
        x: Int =>
           if ((x < 100) && (x > 0))
             Stream.fibs_via_unfold.take(x).toList.last mustEqual fpinscala.gettingstarted.MyModule.fib(x-1)
           else 
             true mustEqual true
        }
      }
    }
    
 //========================================================================================
 //#5.13
  "take_via_unfold" should {
    "return an empty Stream for an empty Stream" in {
      streamEmpty.take_via_unfold(3) mustEqual Empty
    }

    "return a Stream with n first entires" in {
      streamOfInts.take_via_unfold(1).toList mustEqual Stream(1).toList
    }
  }

  "takeWhile_via_unfold" should {
    "return an empty Stream for an empty Stream" in {
      streamEmpty.takeWhile_via_unfold(_ == 1) mustEqual Empty
    }

    "return an empty Stream when the first entry does not satisfy predicate" in {
      streamOfInts.takeWhile_via_unfold(_ == 2) mustEqual Empty
    }

    "return a Stream with n first entires that satisfy predicate" in {
      streamOfInts.takeWhile_via_unfold(_ == 1).toList mustEqual Stream(1).toList
    }
  }

  "zipWith" should {
    "result in one Stream when the other is Empty" in {
      streamOfInts.zipWith(streamEmpty)((x,y) => x+y) mustEqual Empty
    }

    "result in a Stream with f applied on matching elements when Streams have same length" in {
      streamOfInts.zipWith(streamOfInts)(_+_).toList mustEqual List(2,4,6)
    }

    "result in a Stream with f applied on matching elements when Streams have different length" in {
      streamOfInts.zipWith(Stream(1,2,3,4))(_+_).toList mustEqual List(2,4,6)
    }
  }

  "zipAll" should {
    "result in an Empty Stream for two Empty Streams" in {
      streamEmpty.zipAll(streamEmpty).toList mustEqual List()
    }

    "result in a Stream with pairs of two Somes for same length Streams" in {
      Stream(1).zipAll(Stream('a')).toList mustEqual List((Some(1),Some('a')))
    }

    "result in a Stream with pairs of a Some and a None for different length Streams" in {
      Stream().zipAll(Stream('a')).toList mustEqual List((None,Some('a')))
      Stream(1).zipAll(Stream()).toList mustEqual List((Some(1),None))
    }
  }

 //========================================================================================
 //#5.14
  "startsWith" should {
    "return true for Empty sub Stream" in {
      streamOfInts.startsWith(streamEmpty) mustEqual true
    }

    "return false for an Empty sup Stream and a none-empty sub Stream" in {
      streamEmpty.startsWith(streamOfInts) mustEqual false
    }

    "return false when sup Stream doesn't start with sub Stream" in {
      streamOfInts.startsWith(Stream(2)) mustEqual false
    }

    "return true when sup Stream start with sub Stream" in {
      streamOfInts.startsWith(Stream(1,2)) mustEqual true
    }
  }

 //========================================================================================
 //#5.15
  "tails" should {
    "return a Stream of Empty for an Empty Stream" in {
      streamEmpty.tails.toList mustEqual List(Empty)
    }

    "return a Stream of all possible tail Streams" in {
      streamOfInts.tails.map(_.toList).toList mustEqual List(List(1, 2, 3), List(2, 3), List(3), List())
    }
  }

}