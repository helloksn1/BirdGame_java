package birdgame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class BirdGame extends JPanel {
	
	BufferedImage background;
	Ground ground;
	Column column1;
	Column column2;
	Bird bird;
	 //分数
	int score;
	BufferedImage startImage;
	
	/**游戏状态 */
	int state;
	public static final int START = 0;
	public static final int RUNNING = 1;
	public static final int GAME_OVER = 2;
	
	//boolean gameOver;
	BufferedImage gameOverImage;
	
	
	public BirdGame() throws Exception{
		
		    state = START;
			//gameOver = false;
		    startImage = ImageIO.read(getClass().getResource("start.png"));
			gameOverImage = ImageIO.read(getClass().getResource("gameover.png"));
			
			background = ImageIO.read(getClass().getResource("bg.png"));
			ground = new Ground();
			column1 = new Column(1);
			column2 = new Column(2);
			bird = new Bird();
		
	}
	
	
	
	
	
	@Override
    public void paint(Graphics g){
		super.paint(g);
		g.drawImage(background,0,0,null);
		g.drawImage(column1.image,column1.x-column1.width/2,
				column1.y-column1.height/2,null);
		g.drawImage(column2.image,column2.x-column2.width/2,
				column2.y-column2.height/2,null);
		g.drawImage(ground.image,ground.x,ground.y,null);
		//旋转(rotate)绘图坐标系，是API方法
		Graphics2D g2 = (Graphics2D) g;
		g2.rotate(-bird.alpha,bird.x,bird.y);
		g.drawImage(bird.image,bird.x -bird.width/2,
				bird.y-bird.height/2,null);
		g2.rotate(bird.alpha,bird.x,bird.y);
		
		
		//在paint方法中添加绘制分数的算法
		Font f = new Font(Font.SANS_SERIF,Font.BOLD,40);
		g.setFont(f);
		g.drawString(""+score, 40, 60);
		g.setColor(Color.WHITE);
		g.drawString(""+score, 40-3, 60-3);
		
	/**	if(gameOver){
			g.drawImage(gameOverImage,0,0,null);
	}  */
	
		//在paint方法中添加显示游戏结束状态代码
		switch(state){
		case GAME_OVER:
			g.drawImage(gameOverImage,0,0,null);
			break;
		case START:
			g.drawImage(startImage,0,0,null);
			break;
		}
	}
    
	
    //表示游戏流程的控制
	public void action() throws Exception{
		MouseListener l = new MouseAdapter(){
			//鼠标按下
			public void mousePressed(MouseEvent e){
				//鸟向上飞
              //  bird.flappy();
				try{
					switch (state){
					case GAME_OVER:
						column1 = new Column(1);
						column2 = new Column(2);
						bird = new Bird();
						score =0;
						state = START;
						break;
					case START:
						state = RUNNING;
					case RUNNING:
						//鸟向上飞扬
						bird.flappy();
					}
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}
		};
		//将1挂接到当前的面板上
		addMouseListener(l);
		
		
		while(true){
	/**	if(!gameOver){	
			bird.run();
			bird.fly();
			column1.run();
			column2.run();
			ground.run();
			System.out.println(ground.x);
		}
		if(bird.hit(ground) ||bird.hit(column1) ||bird.hit(column2)){
			gameOver = true;
		}
			//计算分数逻辑
			if(bird.x == column1.x ||bird.x == column2.x){
				score++;
				
			} */
		
			switch(state){
			case START:
				bird.fly();
				ground.run();
				break;
			case RUNNING:
				column1.run();
				column2.run();
				bird.run();//上下移动
				bird.fly();//挥动翅膀
				ground.run();//地面移动
				//计分逻辑
				if(bird.x == column1.x || bird.x == column2.x){
					score++;
                                        if (score == sg.bk.id + 3) {
                                            state = START;
                                            sg.birdEnded(true);
                                        }
				}
				//如果鸟撞上地面游戏就结束
				if(bird.hit(ground)||bird.hit(column1)||bird.hit(column2)){
					state = GAME_OVER;
                                        sg.birdEnded(false);
				}
				break;
			}
			repaint();//重画
			Thread.sleep(20);
		}
	}
	
	
	
	
    
    
	public static void main(String [] args) throws Exception {
	JFrame frame = new JFrame();
	BirdGame game = new BirdGame();
	frame.add(game);
	frame.setSize(440,670);//设置宽高
	frame.setResizable(false);//不能窗口化
	frame.setLocationRelativeTo(null);//窗口居中
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//单机关闭使程序真正退出
	frame.setVisible(true);//设置窗体可见性
	game.action();
	}
}
