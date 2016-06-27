package com.hlju.Tetris;

public class BlockV4 {
	static final boolean[][][] Shape = {
			// I
			{ 
				{ false, false, false, false }, 
				{ true, true, true, true }, 
				{ false, false, false, false },
				{ false, false, false, false }
			},
			// J
			{ 
				{ true, false, false },
				{ true, true, true }, 
				{ false, false, false } 
			},
			// L
			{ 
				{ false, false, true }, 
				{ true, true, true }, 
				{ false, false, false }
			},
			// O
			{ 
				{ true, true }, 
				{ true, true } 
			},
			// S
			{ 
				{ false, true, true }, 
				{ true, true, false }, 
				{ false, false, false } 
			},
			// T
			{ 
				{ false, true, false }, 
				{ true, true, true }, 
				{ false, false, false } 
			},
			// Z
			{ 
				{ true, true, false }, 
				{ false, true, true }, 
				{ false, false, false } 
			} };
}
