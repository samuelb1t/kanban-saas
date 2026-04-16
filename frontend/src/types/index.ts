export interface User {
  id: string;
  name: string;
  email: string;
  avatarUrl?: string;
}

export type WorkspaceRole = "Owner" | "Member" | "Viewer";

export interface WorkspaceMember {
  user: User;
  role: WorkspaceRole;
}

export interface Workspace {
  id: string;
  name: string;
  ownerId: string;
  members: WorkspaceMember[];
  boardCount: number;
}

export interface Board {
  id: string;
  workspaceId: string;
  name: string;
  columns: Column[];
}

export interface Column {
  id: string;
  boardId: string;
  name: string;
  order: number;
  tasks: Task[];
}

export interface Task {
  id: string;
  columnId: string;
  title: string;
  description: string;
  order: number;
}

export interface LoginPayload {
  email: string;
  password: string;
}

export interface RegisterPayload {
  name: string;
  email: string;
  password: string;
}
