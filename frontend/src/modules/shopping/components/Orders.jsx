import {FormattedDate, FormattedMessage, FormattedTime} from 'react-intl';
import Table from 'react-bootstrap/Table';

const Orders = ({orders}) => {

    const sortedOrders = orders.slice().sort((a, b) => new Date(b.date) - new Date(a.date));

    return (
        <Table striped hover>
            <thead>
                <tr>
                    <th>
                        <FormattedMessage id='project.shopping.orders.id' />
                    </th>
                    <th>
                        <FormattedMessage id='project.shopping.orders.purchaseDate'/>
                    </th>
                    <th>
                        <FormattedMessage id='project.shopping.orders.movieTitle'/>
                    </th>
                    <th>
                        <FormattedMessage id='project.shopping.orders.quantity'/>
                    </th>
                    <th>
                        <FormattedMessage id='project.shopping.orders.totalPrice'/>
                    </th>
                    <th>
                        <FormattedMessage id='project.shopping.orders.sessionDate'/>
                    </th>
                    <th>
                        <FormattedMessage id='project.shopping.orders.collectedTickets'/>
                    </th>
                </tr>
            </thead>
            <tbody>
                {sortedOrders.map(order => (
                    <tr key={order.id}>
                        <td>{order.id}</td>
                        <td>
                            <FormattedDate value={new Date(order.date)}/>
                            {' '}
                            <FormattedTime value={new Date(order.date)}/>
                        </td>
                        <td>{order.movieTitle}</td>
                        <td>{order.quantity}</td>
                        <td>{`€${order.totalPrice}`}</td>
                        <td>
                            <FormattedDate value={new Date(order.sessionDate)}/>
                            {' '}
                            <FormattedTime value={new Date(order.sessionDate)}/>
                        </td>
                        <td>
                            <FormattedMessage id={order.collectedTickets ?
                                'project.global.values.yes' :
                                'project.global.values.no'} />
                        </td>
                    </tr>
                ))}
            </tbody>
        </Table>
    );

};

export default Orders;
