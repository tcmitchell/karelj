BEGINNING-OF-PROGRAM

  DEFINE-NEW-INSTRUCTION turnright AS
  BEGIN
    ITERATE 3 TIMES
      turnleft
  END;

  DEFINE-NEW-INSTRUCTION sidestep-right AS
  BEGIN
    turnright;
    move;
    turnleft
  END;

  DEFINE-NEW-INSTRUCTION sidestep-back-left AS
  BEGIN
    turnleft;
    move;
    turnright
  END;

  DEFINE-NEW-INSTRUCTION shuffle AS
  BEGIN
    sidestep-right;
    IF front-is-clear
      THEN
        BEGIN
          sidestep-back-left;
          move
        END
  END;

  DEFINE-NEW-INSTRUCTION go-to-wall AS
  BEGIN
    WHILE front-is-clear DO
      IF right-is-blocked
        THEN turnright
        ELSE shuffle
  END;

  DEFINE-NEW-INSTRUCTION follow-perimeter AS
  BEGIN
    IF front-is-clear
      THEN move
      ELSE turnleft
  END;

  DEFINE-NEW-INSTRUCTION follow-until-door-is-on-right AS
  BEGIN
    WHILE right-is-blocked DO
      follow-perimeter
  END;

  DEFINE-NEW-INSTRUCTION exit-door AS
  BEGIN
    turnright;
    move
  END;

  BEGINNING-OF-EXECUTION
    go-to-wall;
    turnleft;
    follow-until-door-is-on-right;
    exit-door;
    turnoff
  END-OF-EXECUTION

END-OF-PROGRAM
