package es.udc.paproject.backend.model.services;

import es.udc.paproject.backend.model.entities.Order;
import es.udc.paproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.paproject.backend.model.entities.User;
import es.udc.paproject.backend.model.exceptions.PermissionException;


public interface PermissionChecker {
	
	User checkUser(Long userId) throws InstanceNotFoundException;

	Order checkOrderExistsAndBelongsTo(Long orderId, Long userId)
			throws PermissionException, InstanceNotFoundException;

}
