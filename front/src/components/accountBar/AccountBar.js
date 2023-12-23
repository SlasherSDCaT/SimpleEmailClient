import "./AccountBar.css"
import { useNavigate } from "react-router-dom";
import {useEffect, useState} from "react";

export default function AccountBar({setMessage, setOneMessage, setShowMessages}) {
    const nav = useNavigate();
    const [authorizedUsers, setAuthorizedUsers] = useState([]);
    const [activeEmail, setActiveEmail] = useState('');

    useEffect(() => {
        // Получаем список пользователей из localStorage при загрузке компонента
        const usersFromStorage = JSON.parse(localStorage.getItem('authorizedUsers')) || {};
        const emails = Object.keys(usersFromStorage);
        setAuthorizedUsers(emails);

        // Получаем активный email из cookies
        const cookies = document.cookie.split(';');
        const userTokenCookie = cookies.find(cookie => cookie.trim().startsWith('userTokens='));
        if (userTokenCookie) {
            const userTokens = JSON.parse(userTokenCookie.split('=')[1]);
            const activeTokenEmail = Object.keys(userTokens)[0]; // Предполагаем, что в cookies только один email
            setActiveEmail(activeTokenEmail);
        }
    }, []);

    const handleEmailClick = (email) => {
        // Получаем токены из localStorage
        const userTokensFromStorage = JSON.parse(localStorage.getItem('authorizedUsers')) || {};

        // Получаем токен для выбранного email
        const tokenForEmail = userTokensFromStorage[email];

        // Обновляем userTokens в куках
        const userToken = { [email]: tokenForEmail };
        document.cookie = `userTokens=${JSON.stringify(userToken)}`;

        // Обновляем активный email в списке
        setActiveEmail(email);
        window.location.reload();
    };


    const [showModal, setShowModal] = useState(false);
    const [emailReceiver, setEmailReceiver] = useState('');
    const [content, setContent] = useState('');
    const handleSendMessage = async () => {
        // Формируем данные для отправки на сервер
        const data = {
            emailReceiver,
            content
        };

        try {
            const response = await fetch('http://localhost:8080/email/message', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${getTokenFromCookies()}`,
                },
                body: JSON.stringify(data),
            });
            console.log(response);
            if (response.ok) {
                setShowModal(false);
            } else {
                console.error('Ошибка при отправке сообщения');
                alert("Неверно введена почта! Скорее всего такой почты не существует(");
            }
        } catch (error) {
            console.error('Ошибка:', error);
        }
        window.location.reload();
    };
    function getTokenFromCookies() {
        const cookieString = document.cookie;
        const cookies = cookieString.split('; ');
        const cookieObj = cookies.reduce((acc, cookie) => {
            const [name, value] = cookie.split('=');
            acc[name] = value;
            return acc;
        }, {});

        return cookieObj['userTokens'].split('":"')[1].slice(0, -1) || '';
    }

    return (<div className="accountBar">
        <button className="button buttonMessage" onClick={() => setShowModal(true)}>Отправить сообщение</button>
        <h3>Учетные записи</h3>
        {authorizedUsers.map(email => (
            <p key={email} className={email === activeEmail ? 'email active' : 'email'} onClick={() => handleEmailClick(email)}>
                {email}
            </p>
        ))}
        <button className="button buttonAccount" onClick={() => nav("/login")}>Добавить учетную запись</button>
        {activeEmail
            ? (<div className="folder">
                <h3>Папки</h3>
                <ul className="folder-list">
                    <li onClick={() => {setMessage("input"); setOneMessage(null); setShowMessages(true)}}>Входящие</li>
                    <li onClick={() => {setMessage("output"); setOneMessage(null); setShowMessages(true)}}>Отправленные</li>
                </ul>
            </div>) : ''}
        {showModal && (
            <div className="modal">
                <div className="modal-place"></div>
                <div className="modal-content">
                    <h2>Отправить сообщение</h2>
                    <form>
                        <input
                            type="email"
                            placeholder="Email получателя"
                            value={emailReceiver}
                            onChange={(e) => setEmailReceiver(e.target.value)}
                            required
                        />
                        <textarea
                            placeholder="Текст сообщения"
                            value={content}
                            onChange={(e) => setContent(e.target.value)}
                            required
                        ></textarea>
                        <div>
                            <button className="sendMessageButton" onClick={(e) => {e.stopPropagation(); handleSendMessage()}}>Отправить</button>
                            <button type="button" className="cancel" onClick={() => setShowModal(false)}>Отмена</button>
                        </div>
                    </form>
                </div>
            </div>
        )}
    </div>);
}