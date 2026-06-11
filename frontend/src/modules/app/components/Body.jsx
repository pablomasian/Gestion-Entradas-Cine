import {useSelector} from 'react-redux';
import {Route, Routes} from 'react-router';
import Container from 'react-bootstrap/Container';

import AppGlobalComponents from './AppGlobalComponents';
import Home from './Home';
import {Login, SignUp, UpdateProfile, ChangePassword, Logout} from '../../users';
import users from '../../users';
import MovieDetails from "../../catalog/components/MovieDetails.jsx";
import SessionDetails from "../../catalog/components/SessionDetails.jsx";
import BuySuccess from "../../catalog/components/BuySuccess.jsx";
import {DeliverTickets, FindOrders, FindOrdersResult} from '../../shopping';

const Body = () => {

    const loggedIn = useSelector(users.selectors.isLoggedIn);
    const role = useSelector(users.selectors.getRole);
    const isSpectator = role === 'ESPECTATOR';
    const isTicketSeller = role === 'TICKETSELLER';
    
   return (

       <Container className="my-4 justify-content-center flex-grow-1">
            <AppGlobalComponents/>
            <Routes>
                <Route path="/*" element={<Home/>}/>
                {loggedIn && <Route path="/users/update-profile" element={<UpdateProfile/>}/>}
                {loggedIn && <Route path="/users/change-password" element={<ChangePassword/>}/>}
                {loggedIn && <Route path="/users/logout" element={<Logout/>}/>}
                {!loggedIn && <Route path="/users/login" element={<Login/>}/>}
                {!loggedIn && <Route path="/users/signup" element={<SignUp/>}/>}
                <Route path="/catalog/movie-details/:id" element={<MovieDetails />}/>
                <Route path="/catalog/session-details/:id" element={<SessionDetails />}/>
                <Route path="/catalog/buy-success/:orderId" element={<BuySuccess />}/>
                {loggedIn && isSpectator && <Route path="/orders" element={<FindOrders/>}/>}
                {loggedIn && isSpectator && <Route path="/orders/find-orders-result" element={<FindOrdersResult/>}/>}
                {loggedIn && isTicketSeller && <Route path="/orders/deliver" element={<DeliverTickets/>}/>}
            </Routes>
       </Container>

    );

};

export default Body;
