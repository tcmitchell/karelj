
;;
;; A While loop
;;
(define-macro (while condition . to-do)
  `(let loop ()
     (if ,condition (begin ,@to-do (loop)))))

(define frame (karel.swingui.KarelFrame. "silk karel"))

(.loadWorld frame)
(.setSize frame 400 400)
(.show frame)

(define *robot* (.fRobot$ frame))

;;----------------------------------------------------------------------
;; robot instructions
;;----------------------------------------------------------------------

(define (put-beeper r)
  (.putBeeper r))

(define (pick-beeper r)
  (.pickBeeper r))

(define (move r)
  (.move r))

(define (turn-around r)
  (.turnleft r)
  (.turnleft r))

(define (backup r)
  (turn-around r)
  (move r)
  (turn-around r))

(define (turnright r)   
  (.turnleft r)
  (.turnleft r)
  (.turnleft r))

(define (find-beeper r)
  (while (.notNextToABeeper r)
	 (if (.rightIsClear r)
	     (turnright r)
	     (while (.frontIsBlocked r)
		    (.turnleft r)))
	 (.move r))
  (.turnoff r))
