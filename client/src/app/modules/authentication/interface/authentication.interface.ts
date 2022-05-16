
export interface SignInPayload {
    username: string;
    password: string;
}

export interface SignUpPayload {
    name: string;
    username: string;
    email: string;
    password: string;
}

export interface JwtResponse {
    id: string;
    username: string;
    email: string;
    roles: string[];
    accessToken: string;
    tokenType: string;
}