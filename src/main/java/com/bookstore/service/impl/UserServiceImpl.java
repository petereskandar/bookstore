package com.bookstore.service.impl;

import java.util.Set;

import org.apache.log4j.spi.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.domain.User;
import com.bookstore.domain.security.PasswordResetToken;
import com.bookstore.domain.security.UserRole;
import com.bookstore.repository.PasswordResetTokenRepository;
import com.bookstore.repository.RoleRepository;
import com.bookstore.repository.UserRepository;
import com.bookstore.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	public PasswordResetTokenRepository passwordResetTokenRepository;
	
	@Autowired
	public UserRepository userRepository;
	
	@Autowired
	public RoleRepository roleRepository;
	
	@Override
	public PasswordResetToken getPasswordResetToken(final String token){
		return passwordResetTokenRepository.findByToken(token);
	}

	@Override
	public void createPasswordResetTokenForUser(User user, String token) {
		final PasswordResetToken myToken = new PasswordResetToken(token, user);
		passwordResetTokenRepository.save(myToken);
		
	}

	@Override
	public User findByUsername(String username) {
		User user = userRepository.findByUsername(username);
		return user;
	}

	@Override
	public User findByUserEmail(String email) {
		User user = userRepository.findByEmail(email);
		return user;
	}

	@Override
	public User createUser(User user, Set<UserRole> userRoles) throws Exception {
		User localUser = userRepository.findByUsername(user.getUsername());
		if(localUser != null)
		{
			LOG.info("user {} already exists, Nothing will be done", user.getUsername());
			
		}else{
			for(UserRole ur : userRoles){
				roleRepository.save(ur.getRole());
			}
			
			user.getUserRoles().addAll(userRoles);
			localUser = userRepository.save(user);
		}
		
		return localUser;
	}
	
	
	
	
}
