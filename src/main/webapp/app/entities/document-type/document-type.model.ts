import { IEmployee } from 'app/entities/employee/employee.model';
import { StateDocument } from 'app/entities/enumerations/state-document.model';

export interface IDocumentType {
  id?: number;
  documentName?: string;
  initials?: string;
  stateDocumentType?: StateDocument;
  documentTypes?: IEmployee[];
}

export class DocumentType implements IDocumentType {
  constructor(
    public id?: number,
    public documentName?: string,
    public initials?: string,
    public stateDocumentType?: StateDocument,
    public documentTypes?: IEmployee[]
  ) {}
}

export function getDocumentTypeIdentifier(documentType: IDocumentType): number | undefined {
  return documentType.id;
}
