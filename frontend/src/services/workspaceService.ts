import type { Workspace, Board } from "@/types";
import { mockWorkspaces, mockBoards } from "./mockData";

export const workspaceService = {
  getWorkspaces: async (): Promise<Workspace[]> => {
    await new Promise((r) => setTimeout(r, 300));
    return mockWorkspaces;
  },

  getWorkspace: async (id: string): Promise<Workspace | undefined> => {
    await new Promise((r) => setTimeout(r, 200));
    return mockWorkspaces.find((w) => w.id === id);
  },

  getBoards: async (workspaceId: string): Promise<Board[]> => {
    await new Promise((r) => setTimeout(r, 200));
    return mockBoards.filter((b) => b.workspaceId === workspaceId);
  },

  getBoard: async (boardId: string): Promise<Board | undefined> => {
    await new Promise((r) => setTimeout(r, 200));
    return mockBoards.find((b) => b.id === boardId);
  },
};
