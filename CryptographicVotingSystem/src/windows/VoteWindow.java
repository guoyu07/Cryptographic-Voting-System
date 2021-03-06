package windows;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;

import java.awt.HeadlessException;
import java.util.List;

import javax.swing.JOptionPane;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;

import com.util.RSAEncrypt;

import dao.Dao;
import dao.model.Candidate;
import dao.model.Voter;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class VoteWindow {

	protected Shell shell;
	private Label lblid;
	private Voter voter;
	private Text text_name;
	private Text text_age;
	private Text text_sex;
	private Text text_id;
	Button button_sure;
	private Text text_c_name;
	private List<Candidate> list = Dao.getCandidateInfo();
	private Candidate candidate;
	private CandidateListWindow c_window;
	public VoteWindow() {}
	
	public VoteWindow(Voter voter) {
		this.voter = voter;
	}
	
	public static void main(String[] args) {
		try {
			VoteWindow window = new VoteWindow();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(437, 335);
		shell.setText("\u4E2A\u4EBA\u4FE1\u606F");
		
		Label label = new Label(shell, SWT.NONE);
		label.setBounds(50, 41, 43, 17);
		label.setText("\u59D3\u540D\uFF1A");
		
		Label label_1 = new Label(shell, SWT.NONE);
		label_1.setText("\u5E74\u9F84\uFF1A");
		label_1.setBounds(50, 85, 43, 17);
		
		Label label_2 = new Label(shell, SWT.NONE);
		label_2.setText("\u6027\u522B\uFF1A");
		label_2.setBounds(50, 135, 43, 17);
		
		lblid = new Label(shell, SWT.NONE);
		lblid.setText("  \u6295\u7968ID\uFF1A");
		lblid.setBounds(27, 182, 66, 17);
		
		text_name = new Text(shell, SWT.BORDER | SWT.READ_ONLY);
		text_name.setEnabled(false);
		text_name.setBounds(99, 35, 108, 23);
		text_name.setText(voter.getName());
		
		text_age = new Text(shell, SWT.BORDER | SWT.READ_ONLY);
		text_age.setEnabled(false);
		text_age.setBounds(99, 79, 108, 23);
		text_age.setText(String.valueOf(voter.getAge()));
		
		text_sex = new Text(shell, SWT.BORDER | SWT.READ_ONLY);
		text_sex.setEnabled(false);
		text_sex.setBounds(99, 129, 108, 23);
		text_sex.setText(voter.getSex());
		
		text_id = new Text(shell, SWT.BORDER | SWT.READ_ONLY);
		text_id.setEnabled(false);
		text_id.setBounds(99, 176, 108, 23);
		text_id.setText(String.valueOf(voter.getId()));
		
		final Button button = new Button(shell, SWT.NONE);
		// 选择候选人
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				button.setEnabled(false);
				c_window = new CandidateListWindow();
				c_window.open();
//				System.out.println("close");
				button.setEnabled(true);
				int ans = c_window.getCandidate_id();
				if(ans != -1){
					candidate = Dao.getCandidatebyId(ans);
					if(candidate == null){
						return;
					}
					text_c_name.setText(candidate.getName());
					button_sure.setEnabled(true);
//					text_name.setText(temp.getName());
//					text_age.setText(String.valueOf(temp.getAge()));
//					text_id.setText(String.valueOf(temp.getId()));
//					text_sex.setText(temp.getSex());
					
				}
			}
		});
		button.setBounds(290, 79, 80, 27);
		button.setText("\u9009\u62E9\u5019\u9009\u4EBA");
		
		button_sure = new Button(shell, SWT.NONE);
		// 发送投票信息
		button_sure.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					System.out.println(voter.getPrivateKey());
					if (voter.getPrivateKey() == null || voter.getPrivateKey().equals("")) {
						JOptionPane.showMessageDialog(null, "请先获得密钥才能投票");
						return;
					}
					if (!Dao.voteCandidate(voter, String.valueOf(candidate.getId()))) {
						JOptionPane.showMessageDialog(null, "不允许重复投票。");
					}
					else {
						JOptionPane.showMessageDialog(null, "投票成功:)");
					}
				} catch (HeadlessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		button_sure.setEnabled(false);
		button_sure.setBounds(174, 233, 80, 27);
		button_sure.setText("\u6295\u7968");
		
		text_c_name = new Text(shell, SWT.BORDER | SWT.READ_ONLY);
		text_c_name.setEnabled(false);
		text_c_name.setText("\u672A\u9009\u62E9\u5019\u9009\u4EBA");
		text_c_name.setBounds(279, 128, 108, 23);
		
		Button button_1 = new Button(shell, SWT.NONE);
		button_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					new ResultWindow();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		button_1.setBounds(279, 176, 108, 27);
		button_1.setText("\u67E5\u770B\u76EE\u524D\u6295\u7968\u7ED3\u679C");
		
		Button button_2 = new Button(shell, SWT.NONE);
		button_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String pk = Dao.getPublicKeyById(voter.getId());
				if (pk != null && !pk.equals("")) {
					JOptionPane.showMessageDialog(null, "你已经有密钥哦亲:)");
					return;
				}
				String[] s = RSAEncrypt.genKeyPair();
				Dao.setKey(voter, s);
				JOptionPane.showMessageDialog(null, "得到密钥完成~");
			}
		});
		button_2.setBounds(290, 35, 80, 27);
		button_2.setText("得到密钥");
	}

	public Candidate getCandidate() {
		return candidate;
	}

	public void setCandidate(Candidate candidate) {
		this.candidate = candidate;
	}
}
