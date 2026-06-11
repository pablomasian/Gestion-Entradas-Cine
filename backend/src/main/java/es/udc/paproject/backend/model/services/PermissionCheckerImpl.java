package es.udc.paproject.backend.model.services;

import java.util.Optional;

import es.udc.paproject.backend.model.entities.Order;
import es.udc.paproject.backend.model.entities.OrderDao;
import es.udc.paproject.backend.model.exceptions.PermissionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.paproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.paproject.backend.model.entities.User;
import es.udc.paproject.backend.model.entities.UserDao;

@Service
@Transactional(readOnly=true)
public class PermissionCheckerImpl implements PermissionChecker {
	
	@Autowired
	private UserDao userDao;

	@Autowired
	private OrderDao orderDao;

	@Override
	public User checkUser(Long userId) throws InstanceNotFoundException {

		Optional<User> user = userDao.findById(userId);
		
		if (user.isEmpty()) {
			throw new InstanceNotFoundException("project.entities.user", userId);
		}
		
		return user.get();
		
	}

	@Override
	public Order checkOrderExistsAndBelongsTo(Long orderId, Long userId) throws PermissionException, InstanceNotFoundException {

		Optional<Order> order = orderDao.findById(orderId);

		if(order.isEmpty()) {
			throw new InstanceNotFoundException("project.entities.order", orderId);
		}

		if(!order.get().getUser().getId().equals(userId)) {
			throw new PermissionException();
		}

		return order.get();
	}

}
