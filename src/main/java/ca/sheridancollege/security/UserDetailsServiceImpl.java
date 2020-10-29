package ca.sheridancollege.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ca.sheridancollege.beans.Role;
import ca.sheridancollege.repositories.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	@Lazy
	private UserRepository uRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//Find the user based on username
		ca.sheridancollege.beans.User user = uRepo.findByUsername(username);
		//if user does not exist throw an exception
		if(user == null) {
			System.out.println("User not found: "+username);
			throw new UsernameNotFoundException("User "+username
					+ "was not found in the database");
		}
		//Change the list of the user's roles into a list of GrantedAuthority
		
		List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
		
		for( Role role : user.getRoles() ) {
			grantList.add(new SimpleGrantedAuthority(role.getRolename()));
		}
			
		//Create a user based on the above information
		UserDetails userDetails = (UserDetails)new User(user.getUsername(),
				user.getEncryptedpassword(), grantList);
		
		return userDetails;
	}

}
