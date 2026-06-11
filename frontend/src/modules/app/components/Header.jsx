import {useSelector} from 'react-redux';
import {Link} from 'react-router';
import {FormattedMessage} from 'react-intl';
import Navbar from 'react-bootstrap/Navbar';
import Nav from 'react-bootstrap/Nav';
import NavDropdown from 'react-bootstrap/NavDropdown';
import Container from "react-bootstrap/Container";

import users from '../../users';

const Header = () => {

    const userName = useSelector(users.selectors.getUserName);
    const role = useSelector(users.selectors.getRole);
    const isSpectator = role === 'ESPECTATOR';
    const isTicketSeller = role === 'TICKETSELLER';

    return (

        <Navbar bg="light" expand="lg" className="border-bottom">
            <Container fluid>
                <Navbar.Brand as={Link} to="/">PA Project</Navbar.Brand>
                <Navbar.Toggle aria-controls="navbarSupportedContent" className="mb-3"/>
                <Navbar.Collapse id="navbarSupportedContent">

                    {userName ? (
                        <Nav className="ms-auto">
                            <NavDropdown title={<><span className="fa-solid fa-user"></span>&nbsp;{userName}</>} align="end" id="user-dropdown">
                                {isSpectator && (
                                    <NavDropdown.Item as={Link} to="/orders">
                                        <FormattedMessage id="project.shopping.header.orders"/>
                                    </NavDropdown.Item>
                                )}
                                {isTicketSeller && (
                                    <NavDropdown.Item id="deliver-tickets" as={Link} to="/orders/deliver">
                                        <FormattedMessage id="project.shopping.header.deliver"/>
                                    </NavDropdown.Item>
                                )}
                                {(isSpectator || isTicketSeller) && <NavDropdown.Divider />}
                                <NavDropdown.Item as={Link} to="/users/update-profile">
                                    <FormattedMessage id="project.users.UpdateProfile.title"/>
                                </NavDropdown.Item>
                                <NavDropdown.Item as={Link} to="/users/change-password">
                                    <FormattedMessage id="project.users.ChangePassword.title"/>
                                </NavDropdown.Item>
                                <NavDropdown.Divider />
                                <NavDropdown.Item as={Link} to="/users/logout">
                                    <FormattedMessage id="project.app.Header.logout"/>
                                </NavDropdown.Item>
                            </NavDropdown>
                        </Nav>
                    ) : (
                        <Nav className="ms-auto">
                            <Nav.Link id="loginLink" as={Link} to="/users/login">
                                <FormattedMessage id="project.users.Login.title"/>
                            </Nav.Link>
                        </Nav>
                    )}
                </Navbar.Collapse>
            </Container>
        </Navbar>

    );

};

export default Header;
