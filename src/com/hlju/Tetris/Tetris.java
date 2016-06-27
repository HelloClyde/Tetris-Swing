package com.hlju.Tetris;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Tetris extends JPanel {

	private static final long serialVersionUID = -807909536278284335L;
	private static final int BlockSize = 10;
	private static final int BlockWidth = 16;
	private static final int BlockHeigth = 26;
	private static final int TimeDelay = 1000;

	private static final String[] AuthorInfo = {
			"制作人：","HelloClyde"
	};

	// 存放已经固定的方块
	private boolean[][] BlockMap = new boolean[BlockHeigth][BlockWidth];

	// 分数
	private int Score = 0;
	
	//是否暂停
	private boolean IsPause = false;

	// 7种形状
	static boolean[][][] Shape = BlockV4.Shape;

	// 下落方块的位置,左上角坐标
	private Point NowBlockPos;

	// 当前方块矩阵
	private boolean[][] NowBlockMap;
	// 下一个方块矩阵
	private boolean[][] NextBlockMap;
	/**
	 * 范围[0,28) 7种，每种有4种旋转状态，共4*7=28 %4获取旋转状态 /4获取形状
	 */
	private int NextBlockState;
	private int NowBlockState;
	
	//计时器
	private Timer timer;

	public Tetris() {
		// TODO 自动生成的构造函数存根
		this.Initial();
		timer = new Timer(Tetris.TimeDelay, this.TimerListener);
		timer.start();
		this.addKeyListener(this.KeyListener);
	}
	
	public void SetMode(String mode){
		if (mode.equals("v6")){
			Tetris.Shape = BlockV6.Shape;
		}
		else{
			Tetris.Shape = BlockV4.Shape;
		}
		this.Initial();
		this.repaint();
	}

	/**
	 * 新的方块落下时的初始化
	 */
	private void getNextBlock() {
		// 将已经生成好的下一次方块赋给当前方块
		this.NowBlockState = this.NextBlockState;
		this.NowBlockMap = this.NextBlockMap;
		// 再次生成下一次方块
		this.NextBlockState = this.CreateNewBlockState();
		this.NextBlockMap = this.getBlockMap(NextBlockState);
		// 计算方块位置
		this.NowBlockPos = this.CalNewBlockInitPos();
	}
	
	/**
	 * 判断正在下落的方块和墙、已经固定的方块是否有接触
	 * @return
	 */
	private boolean IsTouch(boolean[][] SrcNextBlockMap,Point SrcNextBlockPos) {
		for (int i = 0; i < SrcNextBlockMap.length;i ++){
			for (int j = 0;j < SrcNextBlockMap[i].length;j ++){
				if (SrcNextBlockMap[i][j]){
					if (SrcNextBlockPos.y + i >= Tetris.BlockHeigth || SrcNextBlockPos.x + j < 0 || SrcNextBlockPos.x + j >= Tetris.BlockWidth){
						return true;
					}
					else{
						if (SrcNextBlockPos.y + i < 0){
							continue;
						}
						else{
							if (this.BlockMap[SrcNextBlockPos.y + i][SrcNextBlockPos.x + j]){
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * 固定方块到地图
	 */
	private boolean FixBlock(){
		for (int i = 0;i < this.NowBlockMap.length;i ++){
			for (int j = 0;j < this.NowBlockMap[i].length;j ++){
				if (this.NowBlockMap[i][j])
					if (this.NowBlockPos.y + i < 0)
						return false;
					else
						this.BlockMap[this.NowBlockPos.y + i][this.NowBlockPos.x + j] = this.NowBlockMap[i][j];
			}
		}
		return true;
	}
	
	/**
	 * 计算新创建的方块的初始位置
	 * @return 返回坐标
	 */
	private Point CalNewBlockInitPos(){
		return new Point(Tetris.BlockWidth / 2 - this.NowBlockMap[0].length / 2, - this.NowBlockMap.length);
	}

	/**
	 * 初始化
	 */
	public void Initial() {
		//清空Map
		for (int i = 0;i < this.BlockMap.length;i ++){
			for (int j = 0;j < this.BlockMap[i].length;j ++){
				this.BlockMap[i][j] = false;
			}
		}
		//清空分数
		this.Score = 0;
		// 初始化第一次生成的方块和下一次生成的方块
		this.NowBlockState = this.CreateNewBlockState();
		this.NowBlockMap = this.getBlockMap(this.NowBlockState);
		this.NextBlockState = this.CreateNewBlockState();
		this.NextBlockMap = this.getBlockMap(this.NextBlockState);
		// 计算方块位置
		this.NowBlockPos = this.CalNewBlockInitPos();
		this.repaint();
	}
	
	public void SetPause(boolean value){
		this.IsPause = value;
		if (this.IsPause){
			this.timer.stop();
		}
		else{
			this.timer.restart();
		}
		this.repaint();
	}

	/**
	 * 随机生成新方块状态
	 */
	private int CreateNewBlockState() {
		int Sum = Tetris.Shape.length * 4;
		return (int) (Math.random() * 1000) % Sum;
	}

	private boolean[][] getBlockMap(int BlockState) {
		int Shape = BlockState / 4;
		int Arc = BlockState % 4;
		System.out.println(BlockState + "," + Shape + "," + Arc);
		return this.RotateBlock(Tetris.Shape[Shape], Math.PI / 2 * Arc);
	}

	/**
	 * 旋转方块Map，使用极坐标变换,注意源矩阵不会被改变
	 * 使用round解决double转换到int精度丢失导致结果不正确的问题
	 * 
	 * @param BlockMap
	 *            需要旋转的矩阵
	 * @param angel
	 *            rad角度，应该为pi/2的倍数
	 * @return 转换完成后的矩阵引用
	 */
	private boolean[][] RotateBlock(boolean[][] BlockMap, double angel) {
		// 获取矩阵宽高
		int Heigth = BlockMap.length;
		int Width = BlockMap[0].length;
		// 新矩阵存储结果
		boolean[][] ResultBlockMap = new boolean[Heigth][Width];
		// 计算旋转中心
		float CenterX = (Width - 1) / 2f;
		float CenterY = (Heigth - 1) / 2f;
		// 逐点计算变换后的位置
		for (int i = 0; i < BlockMap.length; i++) {
			for (int j = 0; j < BlockMap[i].length; j++) {
				//计算相对于旋转中心的坐标
				float RelativeX = j - CenterX;
				float RelativeY = i - CenterY;
				float ResultX = (float) (Math.cos(angel) * RelativeX - Math.sin(angel) * RelativeY);
				float ResultY = (float) (Math.cos(angel) * RelativeY + Math.sin(angel) * RelativeX);
				/* 调试信息
				System.out.println("RelativeX:" + RelativeX + "RelativeY:" + RelativeY);
				System.out.println("ResultX:" + ResultX + "ResultY:" + ResultY);
				*/
				//将结果坐标还原
				Point OrginPoint = new Point(Math.round(CenterX + ResultX), Math.round(CenterY + ResultY));
				ResultBlockMap[OrginPoint.y][OrginPoint.x] = BlockMap[i][j];
			}
		}
		return ResultBlockMap;
	}

	/**
	 * 测试方法，测试旋转函数
	 * 
	 * @param args
	 */
	static public void main(String... args) {
		boolean[][] SrcMap = Tetris.Shape[3];
		Tetris.ShowMap(SrcMap);
		/*
		for (int i = 0;i < 7;i ++){
			System.out.println(i);
			Tetris.ShowMap(Tetris.Shape[i]);
		}
		*/
		
		Tetris tetris = new Tetris();
		boolean[][] result = tetris.RotateBlock(SrcMap, Math.PI / 2 * 3);
		Tetris.ShowMap(result);
		
	}
	
	/**
	 * 测试方法，显示矩阵
	 * @param SrcMap
	 */
	static private void ShowMap(boolean[][] SrcMap){
		System.out.println("-----");
		for (int i = 0;i < SrcMap.length;i ++){
			for (int j = 0;j < SrcMap[i].length;j ++){
				if (SrcMap[i][j])
					System.out.print("*");
				else
					System.out.print(" ");
			}
			System.out.println();
		}
		System.out.println("-----");
	}

	/**
	 * 绘制游戏界面
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		// 画墙
		for (int i = 0; i < Tetris.BlockHeigth + 1; i++) {
			g.drawRect(0 * Tetris.BlockSize, i * Tetris.BlockSize, Tetris.BlockSize, Tetris.BlockSize);
			g.drawRect((Tetris.BlockWidth + 1) * Tetris.BlockSize, i * Tetris.BlockSize, Tetris.BlockSize,
					Tetris.BlockSize);
		}
		for (int i = 0; i < Tetris.BlockWidth; i++) {
			g.drawRect((1 + i) * Tetris.BlockSize, Tetris.BlockHeigth * Tetris.BlockSize, Tetris.BlockSize,
					Tetris.BlockSize);
		}
		// 画当前方块
		for (int i = 0; i < this.NowBlockMap.length; i++) {
			for (int j = 0; j < this.NowBlockMap[i].length; j++) {
				if (this.NowBlockMap[i][j])
					g.fillRect((1 + this.NowBlockPos.x + j) * Tetris.BlockSize, (this.NowBlockPos.y + i) * Tetris.BlockSize,
						Tetris.BlockSize, Tetris.BlockSize);
			}
		}
		// 画已经固定的方块
		for (int i = 0; i < Tetris.BlockHeigth; i++) {
			for (int j = 0; j < Tetris.BlockWidth; j++) {
				if (this.BlockMap[i][j])
					g.fillRect(Tetris.BlockSize + j * Tetris.BlockSize, i * Tetris.BlockSize, Tetris.BlockSize,
						Tetris.BlockSize);
			}
		}
		//绘制下一个方块
		for (int i = 0;i < this.NextBlockMap.length;i ++){
			for (int j = 0;j < this.NextBlockMap[i].length;j ++){
				if (this.NextBlockMap[i][j])
					g.fillRect(190 + j * Tetris.BlockSize, 30 + i * Tetris.BlockSize, Tetris.BlockSize, Tetris.BlockSize);
			}
		}
		// 绘制其他信息
		g.drawString("游戏分数:" + this.Score, 190, 10);
		for (int i = 0;i < Tetris.AuthorInfo.length;i ++){
			g.drawString(Tetris.AuthorInfo[i], 190, 100 + i * 20);
		}
		
		//绘制暂停
		if (this.IsPause){
			g.setColor(Color.white);
			g.fillRect(70, 100, 50, 20);
			g.setColor(Color.black);
			g.drawRect(70, 100, 50, 20);
			g.drawString("PAUSE", 75, 113);
		}
	}
	/**
	 * 
	 * @return
	 */
	private int ClearLines(){
		int lines = 0;
		for (int i = 0;i < this.BlockMap.length;i ++){
			boolean IsLine = true;
			for (int j = 0;j < this.BlockMap[i].length;j ++){
				if (!this.BlockMap[i][j]){
					IsLine = false;
					break;
				}
			}
			if (IsLine){
				for (int k = i;k > 0;k --){
					this.BlockMap[k] = this.BlockMap[k - 1];
				}
				this.BlockMap[0] = new boolean[Tetris.BlockWidth];
				lines ++;
			}
		}
		return lines;
	}
	
	// 定时器监听
	ActionListener TimerListener = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO 自动生成的方法存根
			if (Tetris.this.IsTouch(Tetris.this.NowBlockMap, new Point(Tetris.this.NowBlockPos.x, Tetris.this.NowBlockPos.y + 1))){
				if (Tetris.this.FixBlock()){
					Tetris.this.Score += Tetris.this.ClearLines() * 10;
					Tetris.this.getNextBlock();
				}
				else{
					JOptionPane.showMessageDialog(Tetris.this.getParent(), "GAME OVER");
					Tetris.this.Initial();
				}
			}
			else{
				Tetris.this.NowBlockPos.y ++;
			}
			Tetris.this.repaint();
		}
	};
	
	//按键监听
	java.awt.event.KeyListener KeyListener = new java.awt.event.KeyListener(){

		@Override
		public void keyPressed(KeyEvent e) {
			// TODO 自动生成的方法存根
			if (!IsPause){
				Point DesPoint;
				switch (e.getKeyCode()) {
				case KeyEvent.VK_DOWN:
					DesPoint = new Point(Tetris.this.NowBlockPos.x, Tetris.this.NowBlockPos.y + 1);
					if (!Tetris.this.IsTouch(Tetris.this.NowBlockMap, DesPoint)){
						Tetris.this.NowBlockPos = DesPoint;
					}
					break;
				case KeyEvent.VK_UP:
					boolean[][] TurnBlock = Tetris.this.RotateBlock(Tetris.this.NowBlockMap,Math.PI / 2);
					if (!Tetris.this.IsTouch(TurnBlock, Tetris.this.NowBlockPos)){
						Tetris.this.NowBlockMap = TurnBlock;
					}
					break;
				case KeyEvent.VK_RIGHT:
					DesPoint = new Point(Tetris.this.NowBlockPos.x + 1, Tetris.this.NowBlockPos.y);
					if (!Tetris.this.IsTouch(Tetris.this.NowBlockMap, DesPoint)){
						Tetris.this.NowBlockPos = DesPoint;
					}
					break;
				case KeyEvent.VK_LEFT:
					DesPoint = new Point(Tetris.this.NowBlockPos.x - 1, Tetris.this.NowBlockPos.y);
					if (!Tetris.this.IsTouch(Tetris.this.NowBlockMap, DesPoint)){
						Tetris.this.NowBlockPos = DesPoint;
					}
					break;
				}
				//System.out.println(Tetris.this.NowBlockPos);
				repaint();
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO 自动生成的方法存根
			
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO 自动生成的方法存根
			
		}
		
	};
}
