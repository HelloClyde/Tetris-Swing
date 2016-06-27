package com.hlju.Tetris;

public class BlockV6 {
	static final boolean[][][] Shape = {
			/*
			 * oooooo
			 */
			{ 
				{ false, false, false, false, false, false }, 
				{ false, false, false, false, false, false }, 
				{ true, true, true, true, true, true }, 
				{ false, false, false, false, false, false }, 
				{ false, false, false, false, false, false }, 
				{ false, false, false, false, false, false }
			},
			/*
			 * ooo
			 * ooo
			 */
			{ 
				{ true, true, true },
				{ true, true, true }, 
				{ false, false, false } 
			},
			/*
			 * ooo
			 *   o
			 *   o
			 *   o
			 */
			{ 
				{ true, true, true , false }, 
				{ false, false, true, false }, 
				{ false, false, true, false }, 
				{ false, false, true, false }
			},
			/*
			 * ooo
			 * o
			 * o
			 * o
			 */
			{ 
				{ false, true, true , true }, 
				{ false, true, false, false }, 
				{ false, true, false, false }, 
				{ false, true, false, false }
			},
			/*
			 *  o
			 *  o
			 * oo
			 * o
			 * o
			 */
			{ 
				{ false, false, true, false, false }, 
				{ false, false, true, false, false }, 
				{ false, true, true, false, false }, 
				{ false, true, false, false, false },
				{ false, true, false, false, false }
			},
			/*
			 * o
			 * o
			 * oo
			 *  o
			 *  o
			 */
			{ 
				{ false, true, false, false, false }, 
				{ false, true, false, false, false }, 
				{ false, true, true, false, false }, 
				{ false, false, true, false, false },
				{ false, false, true, false, false }
			},
			/*
			 * ooo
			 *  o
			 *  o
			 *  o
			 */
			{ 
				{ false, true, true , true }, 
				{ false, false, true, false }, 
				{ false, false, true, false }, 
				{ false, false, true, false }
			},
			/*
			 * ooooo
			 *   o
			 */
			{ 
				{ false, false, false, false, false }, 
				{ true, true, true, true, true }, 
				{ false, false, true, false, false }, 
				{ false, false, true, false, false },
				{ false, false, false, false, false }
			}
			};
}
