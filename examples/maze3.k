
/* karel maze demo: robot will follow the right wall until he finds a
beeper */

BEGINNING-OF-PROGRAM
	DEFINE-NEW-INSTRUCTION turnright AS
	        BEGIN
		        turnleft;
		        turnleft;
		        turnleft
		END;

	BEGINNING-OF-EXECUTION
		turnoff
	END-OF-EXECUTION

END-OF-PROGRAM
