import { HttpInterceptorFn } from '@angular/common/http';
import { environment } from '../../environments/environment';

export const urlInterceptor: HttpInterceptorFn = (req, next) => {
  if (!req.url.startsWith('http')) {
    const apiUrl = environment.url;

    const modifiedReq = req.clone({
      url: `${apiUrl}${req.url}`
    });

    return next(modifiedReq);
  }

  return next(req);
};
