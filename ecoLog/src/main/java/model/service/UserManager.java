package model.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import model.dao.MyMeetingDAO;
import model.dao.UserDAO;
import model.dao.bookMarkDAO;
import model.BookMark;
import model.MyMeeting;
import model.User;
import model.Post;
import model.dao.mybatis.*;

/**
 * 사용자 관리 API를 사용하는 개발자들이 직접 접근하게 되는 클래스.
 * UserDAO를 이용하여 데이터베이스에 데이터 조작 작업이 가능하도록 하며,
 * 데이터베이스의 데이터들을 이용하여 비지니스 로직을 수행하는 역할을 한다.
 * 비지니스 로직이 복잡한 경우에는 비지니스 로직만을 전담하는 클래스를 
 * 별도로 둘 수 있다.
 */
public class UserManager {
	private static final Logger log = LoggerFactory.getLogger(UserManager.class);

	private static UserManager userMan = new UserManager();
	private UserDAO userDAO;
	private bookMarkDAO bmDao;
	private MyMeetingDAO mtDao;  

	public UserManager() {
		try {
			userDAO = new UserDAO();
			bmDao = new bookMarkDAO();
			mtDao = new MyMeetingDAO();
		} catch (Exception e) {
			e.printStackTrace();
		}			
	}

	public static UserManager getInstance() {
		return userMan;
	}

    public int create(User user) throws SQLException, ExistingUserException {
        if (userDAO.existingUser(user.getId()) == true) {
            throw new ExistingUserException(user.getId() + "는 존재하는 아이디입니다.");
        }
        if (userDAO.existingUserNickName(user.getNickname()) == true) {
            throw new ExistingUserException(user.getNickname() + "는 존재하는 닉네임입니다.");
        }
        if (userDAO.existingUserEmail(user.getEmail()) == true) {
            throw new ExistingUserException(user.getEmail() + "는 존재하는 이메일입니다.");
        }
        if (userDAO.existingUserPhoneNumber(user.getphoneNumber()) == true) {
            throw new ExistingUserException(user.getphoneNumber() + "는 존재하는 전화번호입니다.");
        }
        return userDAO.create(user);
    }

    public int update(User user) throws SQLException, UserNotFoundException {

		return userDAO.update(user);
	}	

	public int remove(String userId) throws SQLException, UserNotFoundException {

		return userDAO.remove(userId);
	}
	public User findUser(String Id)
		throws SQLException, UserNotFoundException {
		System.out.println("dao로 출발");
		User user = userDAO.findUser(Id);
		System.out.println("dao에서 user정보 가져옴");

		if (user == null) {
			System.out.println("user가 널");
			throw new UserNotFoundException(Id + "는 존재하지 않는 아이디입니다.");
		}		

		System.out.println("로그인 함수로 전달");
		return user;
	}

	public boolean login(String Id, String password)
		throws SQLException, UserNotFoundException, PasswordMismatchException {
		User user = findUser(Id);
		System.out.println("id찾음");

		if (!user.matchPassword(password)) {
			throw new PasswordMismatchException("비밀번호가 일치하지 않습니다.");
		}
		return true;
	}

	public UserDAO getUserDAO() {
		return this.userDAO;
	}

   public List<User> findUserList() throws SQLException {               
        return userDAO.findUserList();
    }


   public int existingNickname(User user) throws SQLException, ExistingUserException {  
       if (userDAO.existingUser(user.getNickname()) == true) {
           throw new ExistingUserException(user.getNickname() + "는 존재하는 닉네임입니다.");
       }
    return 0;
  }

   public int existingMM(String id, String postNum) throws SQLException, ExistingUserException {  
       if (mtDao.existingMM(id, postNum) == true) {
    	   throw new ExistingUserException("이미 가입한 모임입니다.");
       }
       log.debug("mtDao.existing(id, postNum) -- " + mtDao.existingMM(id, postNum));
    return 0;
  }

   public int existingBM(String id, String postNum) throws SQLException, ExistingUserException {  
	   if (bmDao.existingBM(id, postNum) == true) {
           throw new ExistingUserException("이미 즐겨찾기한 모임입니다.");
       }
	   log.debug("bmDao.existingBM(id, postNum) === " + bmDao.existingBM(id, postNum));
    return 0;
  }
   
	public int removeBookMark(String postNum, String id) throws SQLException {
		return bmDao.removeMyBM(postNum, id);				
	}

	public int removeMyMeeting(String postNum, String id) throws SQLException {
		return mtDao.removeMymeet(postNum, id);					
	}

	public int createBookMark(BookMark bm) throws SQLException {
		return bmDao.create(bm);		
	}

	public int createMyMeeting(MyMeeting mymt) throws SQLException {
		return mtDao.create(mymt);		
	}

	public List<BookMark> getBookMark(String Id) throws SQLException {
		return bmDao.findBookMarkList(Id);		
	}

	public List<MyMeeting> getMyMeeting(String Id) throws SQLException {
		return mtDao.findMyMtList(Id);		
	}
	public Post postInsert(Post post) throws SQLException{
		   return PostDAO.postInsert(post);
	   }
	   
   public int postUpdate(Post post) throws SQLException {
	   return PostDAO.postUpdate(post);
   }
   
   public boolean postDelete(int postNum)throws SQLException {
	   return PostDAO.postDelete(postNum);
   }
   
   public void postView(int postNum) throws SQLException {
	   PostDAO.postDetailData(postNum);
   }
   
   public List<Post> getPostList() throws SQLException {
	   Map map = new HashMap();
	   return PostDAO.postListData(map);
   }
   
   public Post postFind(int postNum) throws SQLException {
	   Map map = new HashMap();
	   return PostDAO.findPost(postNum);
   }
   
   public List<Post> getPostListLast() throws SQLException {
	   Map map = new HashMap();
	   return PostDAO.postListLast(map);
   }
   
   public List<Post> myPostList(String name) throws SQLException {
	   return PostDAO.myPostList(name);
   }
   
   //포인트저장함수 삭제하면 안돼요!!!
   public int savePoint(int point, String Id) throws SQLException {
	   return userDAO.savePoint(point, Id);
   }
   
   public List<User> rankList() throws SQLException {
	   return userDAO.rankList();
   }
   
   public int removeBookMarkPost(String postNum) throws SQLException {
	      return bmDao.remove(postNum);            
	}

   public int removeMyMeetingPost(String postNum) throws SQLException {
      return mtDao.remove(postNum);               
   }
	   
   // 검색은 나중에...
}