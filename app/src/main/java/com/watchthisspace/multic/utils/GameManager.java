package com.watchthisspace.multic.utils;

import android.util.Log;

import com.watchthisspace.multic.config.Globals;
import com.watchthisspace.multic.objects.Board;
import com.watchthisspace.multic.objects.Player;

import java.util.List;

public class GameManager {

    private Board mBoard;

    private List<Player> mPlayers;

    private byte mTurn = 0;

    public GameManager(List<Player> players) {
        mPlayers = players;

        mBoard = new Board(Globals.GAME_BOARD_SIZE);
    }

    public boolean set(int x, int y, byte player) {
        if(mBoard.get(x, y) != Globals.BOARD_EMPTY) {
            return false;
        }

        mBoard.set(x, y, player);
        mBoard.setLastMove(x, y);

        incrementTurn();

        return true;
    }

    // Check for winning condition
    // MONSTER
    public boolean checkMove(int x, int y, byte player) {
        // Possible winning axes
        int col, row, diag, rDiag;
        col = row = diag = rDiag = 1;

        // Flags for search pattern termination
        boolean cp, cn;     // Columns
        boolean rp, rn;     // Rows
        boolean dp, dn;     // Diagonal
        boolean rdp, rdn;   // Reverse diagonal

        cp = cn = rp = rn = dp = dn = rdp = rdn = true;

        for(int i = 1; i < Globals.GAME_WIN_N; i++) {
            // Column checks
            // Positive
            if(cp) {
                if(mBoard.get(x, y + i) == player) {
                    col++;
                } else {
                    cp = false;
                }
            }

            // Negative
            if(cn) {
                if(mBoard.get(x, y - i) == player) {
                    col++;
                } else {
                    cn = false;
                }
            }

            // Row check
            // Positive
            if(rp) {
                if(mBoard.get(x + i, y) == player) {
                    row++;
                } else {
                    rp = false;
                }
            }

            // Negative
            if(rn) {
                if(mBoard.get(x - i, y) == player) {
                    row++;
                } else {
                    rn = false;
                }
            }

            // Diagonal check
            // Positive
            if(dp) {
                if(mBoard.get(x + i, y + i) == player) {
                    diag++;
                } else {
                    dp = false;
                }
            }

            // Negative
            if(dn) {
                if(mBoard.get(x - i, y - i) == player) {
                    diag++;
                } else {
                    dn = false;
                }
            }

            // Reverse diagonal check
            // Positive
            if(rdp) {
                if(mBoard.get(x + i, y - i) == player) {
                    rDiag++;
                } else {
                    rdp = false;
                }
            }

            // Negative
            if(rdn) {
                if(mBoard.get(x - i, y + i) == player) {
                    rDiag++;
                } else {
                    rdn = false;
                }
            }
        }

        if(col >= Globals.GAME_WIN_N ||
                row >= Globals.GAME_WIN_N ||
                diag >= Globals.GAME_WIN_N ||
                rDiag >= Globals.GAME_WIN_N) {

            Log.d("GAME", "PLAYER " + player + " WINS!");
        }

        return true;
    }

    private void incrementTurn() {
        mTurn++;
        if((int) mTurn > mPlayers.size() - 1) {
            mTurn = 0;
        }
    }

    public Board getBoard() {
        return mBoard;
    }

    public List<Player> getPlayers() {
        return mPlayers;
    }

    public byte getTurn() {
        return mTurn;
    }
}
