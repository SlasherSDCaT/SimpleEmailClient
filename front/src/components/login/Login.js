import "./Login.css"
import { useNavigate } from "react-router-dom";
import {useState} from "react";

export default function Login() {
    const nav = useNavigate();
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');

    const handleSubmit = async () => {
        try {
            const response = await fetch('http://localhost:8080/auth/signIn', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ email, password }),
            });

            if (response.ok) {
                const data = await response.json();
                const { token } = data;

                // Добавление в куки
                const userToken = { [email]: token };
                document.cookie = `userTokens=${JSON.stringify(userToken)}`;

                const existingUsers = JSON.parse(localStorage.getItem('authorizedUsers')) || {};
                existingUsers[email] = token;
                localStorage.setItem('authorizedUsers', JSON.stringify(existingUsers));

                nav("/");

            } else {
                setError('Неправильный логин или пароль');
            }
        } catch (error) {
            setError('Неправильный логин или пароль');
            console.error('Ошибка:', error);
        }
    };

    return(<div className="loginPage">
        <h2>Авторизация</h2>
        <form className="loginForm">
            <label htmlFor="email">Email:</label>
            <input
                type="email"
                name="email"
                required
                placeholder="Введите вашу почту"
                value={email}
                onChange={(e) => setEmail(e.target.value)}/>

            <label htmlFor="password">Пароль:</label>
            <input
                type="password"
                className="password"
                name="password"
                required
                placeholder="Введите ваш пароль"
                value={password}
                onChange={(e) => setPassword(e.target.value)}/>
            <div>
                <button type="button" className="sumbits" onClick={handleSubmit}>Войти</button>
                <button type="button" className="cancel" onClick={() => nav("/")}>Отмена</button>
            </div>
            {error && <p className="error">{error}</p>}
        </form>
    </div>);
}