
/* karel maze demo: robot will follow the right wall until he finds a
beeper */

BEGINNING-OF-PROGRAM
	BEGINNING-OF-EXECUTION
		move;
		IF not-next-to-a-beeper THEN
		   turnleft;
		move;
		turnoff
	END-OF-EXECUTION

END-OF-PROGRAM
