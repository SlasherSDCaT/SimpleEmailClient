import "./MainPage.css"
import AccountBar from "../accountBar/AccountBar";
import {useEffect, useState} from "react";
import ShowMessage from "../showMessage/ShowMessage";
import * as events from "events";

export default function MainPage() {
    const [messageData, setMessageData] = useState([]);
    const [messageType, setMessageType] = useState('');
    const [oneMessage, setOneMessage] = useState(null);
    const [mark, setMark] = useState(false);
    const [text, setText] = useState('');
    const [sortedByName, setSortedByName] = useState(false);
    const [token, setToken] = useState('');
    const [showMessage, setShowMessage] = useState(false);
    let hours;
    let minutes;
    let formattedTime;
    if (oneMessage) {
        hours = String(oneMessage.timeDeparture[3]).padStart(2, '0');
        minutes = String(oneMessage.timeDeparture[4]).padStart(2, '0');
        formattedTime = `${oneMessage.timeDeparture[2]}.${oneMessage.timeDeparture[1]}.${oneMessage.timeDeparture[0]} ${hours}:${minutes}`;
    }

    const fetchData = async (typeMessage) => {
        let currentURL;
        getTokenFromCookies()
        if (token !== '') {
            try {
                if (typeMessage === "output") {
                    currentURL = "http://localhost:8080/email/getMessagesIn";
                } else {
                    currentURL = "http://localhost:8080/email/getMessagesOut";
                }
                currentURL += getParams();
                const response =  await  fetch(currentURL, {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                });
                if (response.ok) {
                    const data = await response.json().then();
                    setMessageData(data);
                } else {
                    console.error('Ошибка при получении сообщений');
                }
            } catch (error) {
                console.error('Ошибка:', error);
        }}
    }

    function sendMessage(typeMessage) {
        setMessageType(typeMessage);
        fetchData(typeMessage);
    }


    useEffect(() => {
        fetchData(messageType);
    }, [mark, sortedByName, text]);

    function getTokenFromCookies() {
        const cookieString = document.cookie;
        const cookies = cookieString.split('; ');
        const cookieObj = cookies.reduce((acc, cookie) => {
            const [name, value] = cookie.split('=');
            acc[name] = value;
            return acc;
        }, {});
        if (cookieObj['userTokens']) {
            setToken(cookieObj['userTokens'].split('":"')[1].slice(0, -1) || '');
        }
    }

    function getParams() {
        let temp = "?sortedByName=";
        temp += sortedByName ? "true" : "false";
        temp += mark ? "&mark=true" : "";
        if (text !== '') {
            temp += "&text=";
            temp += text;
        }
        return temp;
    }


    const handleMarkMessage = async (id, mark, typeMessage) => {
        let currentURL;
        getTokenFromCookies()
        if (token !== '') {
            try {
                if (typeMessage === "output") {
                    currentURL = "http://localhost:8080/email/getMessagesIn";
                } else {
                    currentURL = "http://localhost:8080/email/getMessagesOut";
                }
                currentURL += `/${id}?mark=${mark}`;
                const response =  await  fetch(currentURL, {
                    method: 'POST',
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                });
                if (response.ok) {
                    fetchData(typeMessage);
                } else {
                    console.error('Ошибка при получении сообщений');
                }
            } catch (error) {
                console.error('Ошибка:', error);
        }}
    };
    return (<div className="mainPage">
        <header>
            <h1>Почтовый клиент</h1>
            <p>Created by Belosludtsev Egor Pozor, IKBO-16-21, dec 2023</p>
        </header>
        <div className="mainContent">
            <AccountBar setMessage={sendMessage} setOneMessage={setOneMessage} setShowMessages={setShowMessage}/>
            {showMessage ? <ShowMessage
                messages={messageData}
                messageType={messageType}
                setOneMessage={setOneMessage}
                setSortedByName={setSortedByName}
                setMark={setMark}
                mark={mark}
                setText={setText}
                handleMarkMessage={handleMarkMessage}
            /> : ''}
            {oneMessage ? (<div className="oneMessage">
                {messageType === "output"
                    ? <h2>Email получателя: {oneMessage.emailReceiver}</h2>
                    : <h2>Email отправителя: {oneMessage.emailSender}</h2>}
                <h4>{formattedTime}</h4>
                <p>{oneMessage.content}</p>
            </div>) : ''}
        </div>
    </div>);
}