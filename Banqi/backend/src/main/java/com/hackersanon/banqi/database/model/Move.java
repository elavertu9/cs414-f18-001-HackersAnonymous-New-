package com.hackersanon.banqi.database.model;

import com.hackersanon.banqi.board.InvalidCoordinateException;
import com.hackersanon.banqi.board.InvalidMoveException;
import com.hackersanon.banqi.game.MoveType;
import com.sun.istack.NotNull;

import javax.persistence.*;

import static com.hackersanon.banqi.game.MoveType.*;

@Embeddable
public class Move {

    @NotNull
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "row",column = @Column(name = "oRow")),
            @AttributeOverride(name = "column", column = @Column(name = "oColumn"))
    })
    private Coordinate origin;
    @NotNull
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "row",column = @Column(name = "dRow")),
            @AttributeOverride(name = "column", column = @Column(name = "dColumn"))
    })
    private Coordinate destination;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "faceUp", column = @Column(name = "oFaceUp")),
            @AttributeOverride(name = "teamColor",column = @Column(name = "oTeamColor")),
            @AttributeOverride(name = "type", column = @Column(name = "oType"))
    })
    private Piece attacker;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "faceUp", column = @Column(name = "dFaceUp")),
            @AttributeOverride(name = "teamColor",column = @Column(name = "dTeamColor")),
            @AttributeOverride(name = "type", column = @Column(name = "dType"))
    })
    private Piece captured;
    private MoveType moveType;
    private boolean executed;
    
    @NotNull
    private Long activeUser;
    
    public Long getActiveUser()
    {
        return activeUser;
    }
    
    public void setActiveUser(Long activeUser)
    {
        this.activeUser = activeUser;
    }
    
    public void setExecuted(boolean executed)
    {
        this.executed = executed;
    }
    
    public Coordinate getOrigin() {
        return origin;
    }

    public void setOrigin(Coordinate origin) {
        this.origin = origin;
    }

    public Coordinate getDestination() {
        return destination;
    }

    public void setDestination(Coordinate destination) {
        this.destination = destination;
    }

    public Piece getAttacker() {
        return attacker;
    }

    public void setAttacker(Piece attacker) {
        this.attacker = attacker;
    }

    public Piece getCaptured() {
        return captured;
    }

    public void setCaptured(Piece captured) {
        this.captured = captured;
    }

    public MoveType getMoveType() {
        return moveType;
    }

    public void setMoveType(MoveType moveType) {
        this.moveType = moveType;
    }

    public boolean isExecuted() {
        return executed;
    }

    private void setExecuted() {
        this.executed = true;
    }

    public Move executeMove(Board board) throws InvalidMoveException {
        try {
            board.getSquare(getOrigin()).vacateSquare();
            this.setExecuted();
        } catch (InvalidCoordinateException e) {
            e.printStackTrace();
            throw new InvalidMoveException();
        }
        return this;
    }

    public Move executeFlip(Board board) throws InvalidMoveException {
        try {
            board.getSquare(getOrigin()).getPiece().setFaceUp(true);
            this.setExecuted();
        } catch (InvalidCoordinateException e) {
            e.printStackTrace();
            throw new InvalidMoveException();
        }
        return this;
    }
    
    
    public Move attemptMove(Board boardObject) throws InvalidMoveException {
        try {
            return makeMove(boardObject, boardObject.getSquare(this.getOrigin()), boardObject.getSquare(this.getDestination()));
        } catch (InvalidCoordinateException e) {
            throw new InvalidMoveException();
        }
    }
    
    public Move makeMove(Board boardObject, Square origin, Square destination) throws InvalidMoveException {
        Move newMove = new Move();
        this.setMoveType(MoveType.translateToMoveType(origin, destination));
        newMove.setAttacker(origin.getPiece());
        newMove.setCaptured(destination.getPiece());
        if (newMove.getMoveType() == TRAVEL || newMove.getMoveType() == CAPTURE) {
            return newMove.executeMove(boardObject);
        } else if (newMove.getMoveType() == FLIP) {
            return newMove.executeFlip(boardObject);
        }
        return newMove;
    }

}
