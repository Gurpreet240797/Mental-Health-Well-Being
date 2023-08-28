import { useState } from 'react';
import Cookies from 'universal-cookie';

export default function useSession() {

    const getSession = () => JSON.parse(sessionStorage.getItem("session"));

    const [session, setSession] = useState(getSession());

    const storeSession = session => {
        sessionStorage.setItem("session", JSON.stringify(session));
        setSession(session);
        if (session.status) {
            const cookies = new Cookies();
            cookies.set('JSESSIONID', session.id, { path: '/' });
        }
    }

    return {
        session,
        setSession: storeSession
    }
}
